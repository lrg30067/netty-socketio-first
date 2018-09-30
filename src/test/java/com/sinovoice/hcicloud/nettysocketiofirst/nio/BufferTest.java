package com.sinovoice.hcicloud.nettysocketiofirst.nio;

import lombok.extern.slf4j.Slf4j;

import java.nio.IntBuffer;
import java.security.SecureRandom;

@Slf4j
public class BufferTest {
    public static void main(String[] args) {
        // 分配内存大小为10的缓存区
        IntBuffer buffer = IntBuffer.allocate(10);

        log.info("capacity:" + buffer.capacity());

        for (int i = 0; i < 5; ++i) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }

        log.info("before flip limit:" + buffer.limit());

        buffer.flip();

        log.info("after flip limit:" + buffer.limit());

        log.info("enter while loop");

        while (buffer.hasRemaining()) {
            log.info("position:" + buffer.position()
            + "   limit:" + buffer.limit()
            + "   capacity:" + buffer.capacity()
            + "   元素:" + buffer.get());
        }
    }
}
