package com.sinovoice.hcicloud.nettysocketiofirst.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.sinovoice.hcicloud.nettysocketiofirst.common.atomic.AtomicUtil;
import com.sinovoice.hcicloud.nettysocketiofirst.common.atomic.ConcurrentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LoginOutHandler {

    //添加OnDisconnect事件
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        try {
            String sessionId = client.getSessionId().toString();
            log.info("当前用户sessionId为： {}, 下线成功。当前连接数为：{}", sessionId, AtomicUtil.decrement());

            Object object = client.getHandshakeData().getUrlParams().get("ctiCode");
            if (object != null) {
                String ctiCode = ((List<String>) object).get(0).replaceAll("[a-zA-Z]", "");
                ConcurrentUtil.remove(ctiCode);
            }
            log.info("当前hashmap的大小为： {}", ConcurrentUtil.size());
        } catch (Exception e) {
            log.error("SocketIO server onDisconnect error: {}!", e);
        }
    }
}
