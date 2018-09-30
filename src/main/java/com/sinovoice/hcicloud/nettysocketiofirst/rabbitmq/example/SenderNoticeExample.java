package com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.example;

import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.MQAccessBuilder;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.MessageSender;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.DetailRes;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by littlersmall on 16/6/28.
 */
@Service
public class SenderNoticeExample {
    private static final String EXCHANGE = "notice";
    private static final String ROUTING = "fsr-wjyy-wftd";
    private static final String QUEUE = "fsr-wjyy-wftd";

//    @Autowired
//    ConnectionFactory connectionFactory;
//
//    private MessageSender messageSender;
//
//    @PostConstruct
//    public void init() throws IOException, TimeoutException {
//        MQAccessBuilder mqAccessBuilder = new MQAccessBuilder(connectionFactory);
//        messageSender = mqAccessBuilder.buildMessageSender(EXCHANGE, ROUTING, QUEUE);
//    }
//
//    public DetailRes send(Notice notice) {
//        return messageSender.send(notice);
//    }
}
