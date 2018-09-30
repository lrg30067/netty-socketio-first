package com.sinovoice.hcicloud.nettysocketiofirst;

import com.sinovoice.hcicloud.nettysocketiofirst.common.RedisOpr;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class RedisConcurrencyTest {
    // 请求总数
    public static int clientTotal = 40;

    // 同时并发执行的线程数
    public static int threadTotal = 10;

    public static int count = 0;

    public final static String redis_test = "test_list_";
    public final static int border = 8;
    public final static Object object = new Object();
    public static ConcurrentHashMap<String, ReentrantLock> concurrentReenHashMap = new ConcurrentHashMap<>(20000);

    static {
        ReentrantLock lock = null;
        if (!concurrentReenHashMap.containsKey("mhn")) {
            lock = new ReentrantLock();
            concurrentReenHashMap.put("mhn", lock);
        }
    }

    /**
     * redis并发读写测试
     *
     * @throws Exception
     */
    @Test
    public void redisTest() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                ReentrantLock lock = null;
                try {
                    semaphore.acquire();

                    lock = concurrentReenHashMap.get("mhn");
                    lock.lock();
                    RedisOpr.lpush(redis_test, "good");
                    long len = RedisOpr.llen(redis_test);
                    if ((len & (border - 1)) == 0 || len == 1) {
                        log.info("当前的队列长度为：{}, 插入的key为：{}", len);
                    }


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

    @Test
    public void test() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();

                    add();
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
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
