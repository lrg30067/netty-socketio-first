package com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq;


import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.DetailRes;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.MessageWithTime;

/**
 * Created by littlersmall on 16/5/12.
 */
public interface MessageSender {
    DetailRes send(Object message);

    DetailRes send(MessageWithTime messageWithTime);
}
