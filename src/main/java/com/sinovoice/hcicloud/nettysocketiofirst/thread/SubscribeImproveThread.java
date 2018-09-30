package com.sinovoice.hcicloud.nettysocketiofirst.thread;

import com.corundumstudio.socketio.SocketIOServer;
import com.sinovoice.hcicloud.nettysocketiofirst.common.RedisUtil;
import com.sinovoice.hcicloud.nettysocketiofirst.listener.RedisMsgImproveListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.concurrent.Callable;

@Slf4j
public class SubscribeImproveThread  implements Callable<Integer> {

    //用关键字volatile设置变量为线程同步，即这个变量在某一个线程中被修改，其他地方的这个变量的值会被立即修改，这点在这里非常关键，能够在修改标识符时及时退出线程
    public volatile boolean isExit = false;
    private final String channelName = "notice";

    private SocketIOServer server;

    public SubscribeImproveThread(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public Integer call() {
        while (!isExit) {
            //同时订阅redis中的channelName通道 ，收到的内容为 ：106_fsr
            Jedis jedis = RedisUtil.getJedis();
            try {
                RedisMsgImproveListener redisListner = new RedisMsgImproveListener(server);
                jedis.subscribe(redisListner, channelName);

                this.interrupt();
                log.info("Interrupted redis");
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error("SocketIO server redis error: {}!", e.getMessage());
                return 1;
            } finally {
                RedisUtil.returnResource(jedis);
            }
        }
        return 0;
    }

    public void interrupt()
    {
        //复写中断函数，改变exit的状态
        isExit = true;
    }
}
