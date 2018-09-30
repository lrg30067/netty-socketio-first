package com.sinovoice.hcicloud.nettysocketiofirst;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConcurrencyTest {
    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    public static int count = 0;

    public static ConcurrentHashMap<String, ReentrantLock> concurrentReenHashMap = new ConcurrentHashMap<>(20000);

    static {
        ReentrantLock lock = null;
        if (!concurrentReenHashMap.containsKey("mhn")) {
            lock = new ReentrantLock();
            concurrentReenHashMap.put("mhn", lock);
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal ; i++) {

            executorService.execute(() -> {
                ReentrantLock lock = null;
                try {
                    lock = concurrentReenHashMap.get("mhn");
                    lock.lock();
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                lock.unlock();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count:{}", count);
    }

    private static void add() {
        count++;
    }


}
