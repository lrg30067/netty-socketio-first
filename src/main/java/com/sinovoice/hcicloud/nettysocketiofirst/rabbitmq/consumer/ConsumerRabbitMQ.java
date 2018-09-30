package com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.consumer;

import com.corundumstudio.socketio.SocketIOServer;
import com.sinovoice.hcicloud.nettysocketiofirst.handler.NoticeFWWHandler;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.MQAccessBuilder;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.MessageProcess;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.ThreadPoolConsumer;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.Constants;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Notice;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

//@Service
public class ConsumerRabbitMQ {
//    private static final String EXCHANGE = "notice";
//    private static final String ROUTING = "fsr-wjyy-wftd";
//    private static final String QUEUE = "fsr-wjyy-wftd";
//
//    @Autowired
//    ConnectionFactory connectionFactory;
//
//    @Autowired
//    SocketIOServer server;
//
//    private ThreadPoolConsumer<Notice> threadPoolConsumer;
//
//    @PostConstruct
//    public void init() {
//        MQAccessBuilder mqAccessBuilder = new MQAccessBuilder(connectionFactory);
//        MessageProcess<Notice> messageProcess = new NoticeFWWHandler(server);
//
//        threadPoolConsumer = new ThreadPoolConsumer.ThreadPoolConsumerBuilder<Notice>()
//                .setThreadCount(Constants.THREAD_COUNT).setIntervalMils(Constants.INTERVAL_MILS)
//                .setExchange(EXCHANGE).setRoutingKey(ROUTING).setQueue(QUEUE)
//                .setMQAccessBuilder(mqAccessBuilder).setMessageProcess(messageProcess)
//                .build();
//    }
//
//    // 启动消费者
//    public void start() throws IOException {
//        threadPoolConsumer.start();
//    }
//
//    public void stop() {
//        threadPoolConsumer.stop();
//    }



//    public static void main(String[] args) throws IOException {
//        poolExample.start();
//        new Thread(new Runnable() {
//            int id = 0;
//
//            @Override
//            public void run() {
//                while (true) {
//                    if (id == 10) {
//                        break;
//                    }
//                    senderExample.send(new UserMessage(id++, "" + System.nanoTime()));
//
//                    try {
//                        Thread.sleep(1000L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }
}
