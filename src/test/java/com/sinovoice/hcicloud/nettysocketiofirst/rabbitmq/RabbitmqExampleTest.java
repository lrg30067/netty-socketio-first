package com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq;

import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.consumer.ConsumerRabbitMQ;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.example.ConsumerExample;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.example.SenderExample;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.example.SenderNoticeExample;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.example.UserMessage;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Notice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: 遇见小星
 * Email: tengxing7452@163.com
 * Date: 17-6-16
 * Time: 下午12:18
 * Describe: member应用测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RabbitmqExampleTest {
    /**
     * Spring RestTemplate的便利替代。你可以获取一个普通的或发送基本HTTP认证（使用用户名和密码）的模板
     * 这里不使用
     */
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Autowired
//    SenderExample senderExample;
//
//    @Autowired
//    SenderNoticeExample senderNoticeExample;
//
//    @Autowired
//    ConsumerExample consumerExample;
//
//    @Autowired
//    ConsumerRabbitMQ consumerRabbitMQ;
//
//    @Test
//    public void test(){
//        Map<String,Object> map = new HashMap();
//        map.put("start",0);
//        map.put("size",8);
//        System.out.println(map);
//        System.out.println("-----测试完毕-------");
//    }
//
//    @Test
//    public void senderExampleTest(){
//        System.out.println("-----发送到rabbitmq-------");
//        UserMessage userMessage = new UserMessage();
//        userMessage.setId(1002);
//        userMessage.setName("hello mhn");
//        senderExample.send(userMessage);
//    }
//
//    @Test
//    public void senderNoticeExampleTest() throws InterruptedException {
//        System.out.println("-----发送到rabbitmq-------");
//
//        Notice notice = new Notice();
//        notice.setCtiCode("106");
//        notice.setFsrNum(1);
//
//        Thread thread = new Thread(new Runnable() {
//            int id = 0;
//            @Override
//            public void run() {
//                while (true) {
//                    if (id == 100) {
//                        break;
//                    }
//                    id++;
//                    senderNoticeExample.send(notice);
////                    try {
////                        Thread.sleep(1000L);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                }
//            }
//        });
//        thread.start();
//        thread.join();
//
//        System.out.println("-----发送完毕-------");
//    }
//
//    @Test
//    public void consumerExampleTest(){
//        System.out.println("-----消费rabbitmq数据-------");
//        consumerExample.consume();
//    }
//
//    @Test
//    public void consumerRabbitMQTest() throws IOException {
//        System.out.println("-----消费rabbitmq数据-------");
////        consumerRabbitMQ.start();
//        System.out.println("-----测试完毕-------");
//    }
}
