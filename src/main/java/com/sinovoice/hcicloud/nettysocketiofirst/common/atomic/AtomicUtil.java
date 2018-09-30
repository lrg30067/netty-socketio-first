package com.sinovoice.hcicloud.nettysocketiofirst.common.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器
 */
@Slf4j
public class AtomicUtil {

    private static AtomicInteger count = new AtomicInteger(0);

    public static int add() {
        return count.incrementAndGet();
    }

    public static int decrement() {
        return count.decrementAndGet();
    }
}
