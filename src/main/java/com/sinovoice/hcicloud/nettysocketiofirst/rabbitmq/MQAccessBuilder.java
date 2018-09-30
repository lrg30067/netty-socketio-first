package com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq;

import com.rabbitmq.client.*;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.Constants;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.DetailRes;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.MessageWithTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by littlersmall on 16/5/11.
 */
@Slf4j
public class MQAccessBuilder {
    private ConnectionFactory connectionFactory;

    public MQAccessBuilder(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public MessageSender buildMessageSender(final String exchange, final String routingKey, final String queue) throws IOException {
        return buildMessageSender(exchange, routingKey, queue, "direct");
    }

    public MessageSender buildTopicMessageSender(final String exchange, final String routingKey) throws IOException {
        return buildMessageSender(exchange, routingKey, null, "topic");
    }

    //1 构造template, exchange, routingkey等
    //2 设置message序列化方法
    //3 设置发送确认，代表：生产者发送消息到exchange成功的回调方法
    //4 构造sender方法
    //5 消息exchange与queue绑定失败，回调方法
    public MessageSender buildMessageSender(final String exchange, final String routingKey,
                                            final String queue, final String type) throws IOException {
        Connection connection = connectionFactory.createConnection();
        //1
        if (type.equals("direct")) {
            buildQueue(exchange, routingKey, queue, connection, "direct");
        } else if (type.equals("topic")) {
            buildTopic(exchange, connection);
        }

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 当mandatory标志位设置为true时，如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，
        // 那么会调用basic.return方法将消息返回给生产者（Basic.Return + Content-Header + Content-Body）；
        // 当mandatory设置为false时，出现上述情形broker会直接将消息扔掉。
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setRoutingKey(routingKey);
        //2
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        RetryCache retryCache = new RetryCache();

        //3
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.info("send message failed: " + cause + correlationData.toString());
            } else {
                retryCache.del(Long.valueOf(correlationData.getId()));
            }
        });
        //5
        /**
         * 实现此接口以通知交付basicpublish失败时，“mandatory”或“immediate”的标志监听（源代码注释翻译）。
         * 在发布消息时设置mandatory等于true，监听消息是否有相匹配的队列，
         * 没有时ReturnListener将执行handleReturn方法，消息将返给发送者
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, tmpExchange, tmpRoutingKey) -> {
            try {
                Thread.sleep(Constants.ONE_SECOND);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("send message failed: " + replyCode + " " + replyText);
            rabbitTemplate.send(message);
        });

        //4
        return new MessageSender() {
            {
                retryCache.setSender(this);
            }

            @Override
            public DetailRes send(Object message) {
                long id = retryCache.generateId();
                long time = System.currentTimeMillis();

                return send(new MessageWithTime(id, time, message));
            }

            @Override
            public DetailRes send(MessageWithTime messageWithTime) {
                try {
                    retryCache.add(messageWithTime);
                    rabbitTemplate.correlationConvertAndSend(messageWithTime.getMessage(),
                            new CorrelationData(String.valueOf(messageWithTime.getId())));
                } catch (Exception e) {
                    return new DetailRes(false, "");
                }

                return new DetailRes(true, "");
            }
        };
    }

    public <T> MessageConsumer buildMessageConsumer(String exchange, String routingKey, final String queue,
                                                    final MessageProcess<T> messageProcess) throws IOException {
        return buildMessageConsumer(exchange, routingKey, queue, messageProcess, "direct");
    }

    public <T> MessageConsumer buildTopicMessageConsumer(String exchange, String routingKey, final String queue,
                                                         final MessageProcess<T> messageProcess) throws IOException {
        return buildMessageConsumer(exchange, routingKey, queue, messageProcess, "topic");
    }

    //1 创建连接和channel
    //2 设置message序列化方法
    //3 consume
    public <T> MessageConsumer buildMessageConsumer(String exchange, String routingKey, final String queue,
                                                     final MessageProcess<T> messageProcess, String type) throws IOException {
        final Connection connection = connectionFactory.createConnection();

        //1
        buildQueue(exchange, routingKey, queue, connection, type);

        //2
        final MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();
        final MessageConverter messageConverter = new Jackson2JsonMessageConverter();

        //3
        return new MessageConsumer() {
            Channel channel;

            {
                channel = connection.createChannel(false);
            }

            //1 通过basicGet获取原始数据，将false自送提交关闭
            //2 将原始数据转换为特定类型的包
            //3 处理数据
            //4 手动发送ack确认
            @Override
            public DetailRes consume() {
                try {
                    //1
                    GetResponse response = channel.basicGet(queue, false);

                    while (response == null) {
                        response = channel.basicGet(queue, false);
                        Thread.sleep(Constants.ONE_SECOND);
                    }

                    Message message = new Message(response.getBody(),
                            messagePropertiesConverter.toMessageProperties(response.getProps(), response.getEnvelope(), "UTF-8"));

                    //2
                    @SuppressWarnings("unchecked")
                    T messageBean = (T) messageConverter.fromMessage(message);

                    //3
                    DetailRes detailRes;

                    try {
                        detailRes = messageProcess.process(messageBean);
                    } catch (Exception e) {
                        log.error("exception", e);
                        detailRes = new DetailRes(false, "process exception: " + e);
                    }

                    //4
                    if (detailRes.isSuccess()) {
                        /**
                         consumer处理成功后，通知broker删除队列中的消息，如果设置multiple=true，表示支持批量确认机制以减少网络流量。
                         例如：有值为5,6,7,8 deliveryTag的投递
                         如果此时channel.basicAck(8, true);则表示前面未确认的5,6,7投递也一起确认处理完毕。
                         如果此时channel.basicAck(8, false);则仅表示deliveryTag=8的消息已经成功处理。
                         */
                        channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
                    } else {
                        //避免过多失败log
                        Thread.sleep(Constants.ONE_SECOND);
                        log.info("process message failed: " + detailRes.getErrMsg());

                        /**
                         consumer处理失败后，例如：有值为5,6,7,8 deliveryTag的投递。
                         如果channel.basicNack(8, true, true);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息重新放回队列中。
                         如果channel.basicNack(8, true, false);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息直接丢弃。
                         如果channel.basicNack(8, false, true);表示deliveryTag=8的消息处理失败且将该消息重新放回队列。
                         如果channel.basicNack(8, false, false);表示deliveryTag=8的消息处理失败且将该消息直接丢弃。
                         */
                        channel.basicNack(response.getEnvelope().getDeliveryTag(), false, true);
                    }

                    return detailRes;
                } catch (InterruptedException e) {
                    log.error("exception", e);
                    return new DetailRes(false, "interrupted exception " + e.toString());
                } catch (ShutdownSignalException | ConsumerCancelledException | IOException e) {
                    log.error("exception", e);

                    try {
                        channel.close();
                    } catch (IOException | TimeoutException ex) {
                        log.error("exception", ex);
                    }

                    channel = connection.createChannel(false);

                    return new DetailRes(false, "shutdown or cancelled exception " + e.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("exception : ", e);

                    try {
                        channel.close();
                    } catch (IOException | TimeoutException ex) {
                        ex.printStackTrace();
                    }

                    channel = connection.createChannel(false);

                    return new DetailRes(false, "exception " + e.toString());
                }
            }
        };
    }

    private void buildQueue(String exchange, String routingKey,
                            final String queue, Connection connection, String type) throws IOException {
        Channel channel = connection.createChannel(false);

        if (type.equals("direct")) {
            /**
             * 声明一个 exchange.
             * @param exchange 名称
             * @param type  exchange type：direct、fanout、topic、headers
             * @param durable 持久化
             * @param autoDelete 是否自动删除（没有连接自动删除）
             * @param arguments 队列的其他属性(构造参数)
             * @return 成功地声明了一个声明确认方法来指示交换。
             * @throws IOException if an error is encountered
             */
            channel.exchangeDeclare(exchange, "direct", true, false, null);
        } else if (type.equals("topic")) {
            channel.exchangeDeclare(exchange, "topic", true, false, null);
        }

        /**
         *申明一个队列，如果这个队列不存在，将会被创建
         * @param queue 队列名称
         * @param durable 持久性：true队列会再重启过后存在，但是其中的消息不会存在。
         * @param exclusive 是否只能由创建者使用
         * @param autoDelete 是否自动删除（没有连接自动删除）
         * @param arguments 队列的其他属性(构造参数)
         * @return 宣告队列的声明确认方法已成功声明。
         * @throws IOException if an error is encountered
         */
        channel.queueDeclare(queue, true, false, false, null);

        /**
         * 将队列绑定到Exchange，不需要额外的参数。
         * @param queue 队列名称
         * @param exchange 交换机名称
         * @param routingKey 路由关键字
         * @return Queue.BindOk：如果成功创建绑定，则返回绑定确认方法。
         * @throws IOException if an error is encountered
         */
        channel.queueBind(queue, exchange, routingKey);

        try {
            channel.close();
        } catch (TimeoutException e) {
            log.info("close channel time out ", e);
        }
    }

    private void buildTopic(String exchange, Connection connection) throws IOException {
        Channel channel = connection.createChannel(false);
        channel.exchangeDeclare(exchange, "topic", true, false, null);
    }

    //for test
    public int getMessageCount(final String queue) throws IOException {
        Connection connection = connectionFactory.createConnection();
        final Channel channel = connection.createChannel(false);
        final AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queue);

        return declareOk.getMessageCount();
    }
}
