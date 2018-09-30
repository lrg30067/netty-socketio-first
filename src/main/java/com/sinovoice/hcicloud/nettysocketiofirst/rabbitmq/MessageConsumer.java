package com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq;


import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.DetailRes;

/**
 * Created by littlersmall on 16/5/12.
 */
public interface MessageConsumer {
    DetailRes consume();
}
