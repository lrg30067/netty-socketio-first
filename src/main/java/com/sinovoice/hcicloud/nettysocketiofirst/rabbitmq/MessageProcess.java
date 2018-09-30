package com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq;


import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.DetailRes;

/**
 * Created by littlersmall on 16/5/11.
 */
public interface MessageProcess<T> {
    DetailRes process(T message);
}
