package com.sinovoice.hcicloud.nettysocketiofirst.start;

import com.corundumstudio.socketio.SocketIOServer;
import com.sinovoice.hcicloud.nettysocketiofirst.thread.SubscribeImproveThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * SpringBoot启动之后执行,启动redis的监听
 *
 * @author mhn
 * @version 1.0
 * @since 2018年6月10日 13:56:26
 */
@Component
@Order(3)
@Slf4j
public class SubscribeRunner implements CommandLineRunner {

    @Autowired
    SocketIOServer server;

    @Override
    public void run(String... args) throws Exception {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        SubscribeImproveThread subscribeProThread = new SubscribeImproveThread(server);
        FutureTask<Integer> future = new FutureTask<Integer>(subscribeProThread);
        exec.execute(future);
    }
}
