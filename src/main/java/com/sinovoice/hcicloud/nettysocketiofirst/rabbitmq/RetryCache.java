package com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq;

import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.Constants;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.DetailRes;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.MessageWithTime;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by littlersmall on 16/9/5.
 * 会重试三次
 */
@Slf4j
public class RetryCache {
    private MessageSender sender;
    private boolean stop = false;
    private Map<Long, MessageWithTime> map = new ConcurrentHashMap<>();
    private AtomicLong id = new AtomicLong();

    public void setSender(MessageSender sender) {
        this.sender = sender;
        startRetry();
    }

    public long generateId() {
        return id.incrementAndGet();
    }

    public void add(MessageWithTime messageWithTime) {
        map.putIfAbsent(messageWithTime.getId(), messageWithTime);
    }

    public void del(long id) {
        map.remove(id);
    }

    private void startRetry() {
        new Thread(() ->{
            while (!stop) {
                try {
                    Thread.sleep(Constants.RETRY_TIME_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long now = System.currentTimeMillis();

                for (Map.Entry<Long, MessageWithTime> entry : map.entrySet()) {
                    MessageWithTime messageWithTime = entry.getValue();

                    if (null != messageWithTime) {
                        if (messageWithTime.getTime() + 3 * Constants.VALID_TIME < now) {
                            log.info("send message {} failed after 3 min ", messageWithTime);
                            del(entry.getKey());
                        } else if (messageWithTime.getTime() + Constants.VALID_TIME < now) {
                            DetailRes res = sender.send(messageWithTime);

                            if (!res.isSuccess()) {
                                log.info("retry send message failed {} errMsg {}", messageWithTime, res.getErrMsg());
                            }
                        }
                    }
                }
            }
        }).start();
    }
}
