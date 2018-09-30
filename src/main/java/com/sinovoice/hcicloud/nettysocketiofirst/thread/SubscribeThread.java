package com.sinovoice.hcicloud.nettysocketiofirst.thread;

import com.sinovoice.hcicloud.nettysocketiofirst.common.RedisUtil;
import com.sinovoice.hcicloud.nettysocketiofirst.handler.NoticeEventHandler;
import com.sinovoice.hcicloud.nettysocketiofirst.listener.RedisMsgPubSubListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 2018年9月28日 21:13:06
 * 升级到 3.0 版本，这个版本就废弃了
 */
public class SubscribeThread implements Runnable {

    //日志
    private static Logger log = LoggerFactory.getLogger(SubscribeThread.class);
    //用关键字volatile设置变量为线程同步，即这个变量在某一个线程中被修改，其他地方的这个变量的值会被立即修改，这点在这里非常关键，能够在修改标识符时及时退出线程
    public volatile boolean isExit = false;


    private String sessionId;
    private String ctiCode;
    private String tipUrl;

    public SubscribeThread(String sessionId, String ctiCode, String tipUrl) {
        this.sessionId = sessionId;
        this.ctiCode = ctiCode;
        this.tipUrl = tipUrl;
    }

    @Override
    public void run() {
        while (!isExit) {
            //同时订阅redis中的ctiCode
            Jedis jedis = RedisUtil.getJedis();
            try {
                RedisMsgPubSubListener redisListner = new RedisMsgPubSubListener(sessionId, ctiCode, tipUrl);

                NoticeEventHandler.clientCtiCodeMap.put(sessionId, redisListner);

                jedis.subscribe(redisListner, ctiCode);

                this.interrupt();
                System.out.println("Interrupted redis");
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error("SocketIO server redis error: {}!", e);
            } finally {
                RedisUtil.returnResource(jedis);
            }
        }
    }

    public void interrupt()
    {
        //复写中断函数，改变exit的状态
        isExit = true;
    }
}
