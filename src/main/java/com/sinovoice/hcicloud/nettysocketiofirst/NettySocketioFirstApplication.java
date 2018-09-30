package com.sinovoice.hcicloud.nettysocketiofirst;

import com.sinovoice.hcicloud.nettysocketiofirst.thread.SubscribeImproveThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@SpringBootApplication
public class NettySocketioFirstApplication {

    public static void main(String[] args) throws IOException {
        //todo 正式环境启动配置项 java -Xms2048m -Xmx5120m -Xss512K -XX:PermSize=512m -XX:MaxPermSize=1024m -jar xxx.jar
        //测试地址： http://localhost:7902/index_send.html
        SpringApplication.run(NettySocketioFirstApplication.class, args);
    }
}
