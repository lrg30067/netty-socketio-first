package com.sinovoice.hcicloud.nettysocketiofirst.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.sinovoice.hcicloud.nettysocketiofirst.common.atomic.ConcurrentUtil;
import com.sinovoice.hcicloud.nettysocketiofirst.handler.NoticeEventHandler;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Notice;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

@Slf4j
public class RedisMsgImproveListener extends JedisPubSub{

    private final String channelName = "notice";
    private final String tipUrl = "http://nets-itsadmin-shydhw-test1.paic.com.cn:18080/pingan_sap";

    private SocketIOServer server;
    public RedisMsgImproveListener(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void onMessage(@NonNull String s, String s1) {
        log.info(String.format("receive redis published message, channelName %s, message %s", s, s1));
        if (s.equals(channelName)){
            if (StringUtils.isNotEmpty(s1)) {
                String[] responseArray = s1.split("_");
                String ctiCode = responseArray[0];
                String message = responseArray[1];

                Notice notice = new Notice();
                notice.setCtiCode(ctiCode);
                notice.setTipUrl(tipUrl);
                if ("wjyy".equals(message)){
                    notice.setWjyyNum(1);
                }
                else if ("fsr".equals(message))
                {
                    notice.setFsrNum(1);
                }
                else if ("fwtd".equals(message))
                {
                    notice.setFwtdNum(1);
                }

                try {
                    UUID sessionId = (UUID) ConcurrentUtil.get(ctiCode);
                    SocketIOClient client = server.getClient(sessionId);
                    client.sendEvent("notice_event", notice);

                    log.info("当前用户sessionId为：{}", sessionId);
                    log.info("收到服务的响应为：{}", notice);
                    log.info("当前在线用户数为：{}", ConcurrentUtil.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPMessage(String s, String s1, String s2) {

    }

    @Override
    public void onSubscribe(String s, int i) {
        log.info(String.format("subscribe redis channel success, channel %s, subscribedChannels %d", s, i));
    }

    @Override
    public void onUnsubscribe(String s, int i) {
        log.info(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d", s, i));
    }

    @Override
    public void onPUnsubscribe(String s, int i) {

    }

    @Override
    public void onPSubscribe(String s, int i) {

    }
}
