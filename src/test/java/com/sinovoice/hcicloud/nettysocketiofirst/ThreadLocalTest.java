package com.sinovoice.hcicloud.nettysocketiofirst;

import org.junit.Test;

import java.util.LinkedHashMap;

public class ThreadLocalTest {
    private final static ThreadLocal<Long> longLocal = new ThreadLocal<Long>();
    private final static ThreadLocal<String> stringLocal = new ThreadLocal<String>();

    public void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public Long getLong() {
        return longLocal.get();
    }

    public String getString() {
        return stringLocal.get();
    }


    @Test
    public void threadLocalTest() throws Exception {
        ThreadLocalTest test = new ThreadLocalTest();
        System.out.println(test.getLong());
    }

    @Test
    public void threadLocalTest1() throws InterruptedException {
        final ThreadLocalTest test = new ThreadLocalTest();

        System.out.println(test.getLong());
        System.out.println(test.getString());

//        Thread thread1 = new Thread(){
//            public void run() {
//                test.set();
//                System.out.println(test.getLong());
//                System.out.println(test.getString());
//            }
//        };
//        thread1.start();
//        thread1.join();
//
//        System.out.println(test.getLong());
//        System.out.println(test.getString());
    }

}
