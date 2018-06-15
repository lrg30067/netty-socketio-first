package com.sinovoice.hcicloud.nettysocketiofirst.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.sinovoice.hcicloud.nettysocketiofirst.handler.NoticeEventHandler;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Notice;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;


public class RedisMsgPubSubListener extends JedisPubSub{

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RedisMsgPubSubListener.class);

    private String sessionId;
    private String channel;
    private String tipUrl;

    public RedisMsgPubSubListener (String sessionId, String channel, String tipUrl){
        this.sessionId = sessionId;
        this.channel = channel;
        this.tipUrl = tipUrl;
    }


    @Override
    public void onMessage(String s, String s1) {
        log.info(String.format("receive redis published message, channel %s, message %s", s, s1));

        if (s.equals(channel)){
            Notice notice = new Notice();
            notice.setCtiCode(channel);
            notice.setTipUrl(tipUrl);

            if ("wjyy".equals(s1)){
                notice.setWjyyNum(1);
            }
            else if ("fsr".equals(s1))
            {
                notice.setFsrNum(1);
            }
            else if ("fwtd".equals(s1))
            {
                notice.setFwtdNum(1);
            }
            try {
                SocketIOClient client = NoticeEventHandler.clientsMap.get(sessionId);
                if (client != null){
                    client.sendEvent("notice_event", notice);
                    log.info(String.format("current lines number : %s ,send data %s", NoticeEventHandler.clientsMap.size() ,notice.toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
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

//    @Override
//    public void onData(SocketIOClient socketIOClient, Notice notice, AckRequest ackRequest) throws Exception {
//
////        this.server.getBroadcastOperations().sendEvent("notice_event", notice);
//        socketIOClient.sendEvent("notice_event", notice);
//
//    }
}
