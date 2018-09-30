package com.sinovoice.hcicloud.nettysocketiofirst.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.sinovoice.hcicloud.nettysocketiofirst.common.atomic.ConcurrentUtil;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.MessageProcess;
import com.sinovoice.hcicloud.nettysocketiofirst.rabbitmq.common.DetailRes;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Notice;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * TODO mq的消息队列方式暂不启用
 * Author: mhn
 * Date:  2018年9月11日 17:38:08
 * Describe: 接收 fsr(防骚扰), wjyy(违禁用语), fwtd(服务态度) 的消息提示
 */
@Slf4j
public class NoticeFWWHandler implements MessageProcess<Notice> {

    private SocketIOServer server;

    public NoticeFWWHandler(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public DetailRes process(Notice notice){
        try {
            String ctiCode = notice.getCtiCode();
            UUID sessionId = (UUID) ConcurrentUtil.get(ctiCode);
            SocketIOClient client = server.getClient(sessionId);
            client.sendEvent("notice_event", notice);
            log.info("当前用户sessionId为：{}，收到服务的响应为：{}", sessionId, notice);
            return new DetailRes(true, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new DetailRes(false, e.getMessage());
        }
    }

}
