package com.sinovoice.hcicloud.nettysocketiofirst.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.sinovoice.hcicloud.nettysocketiofirst.common.atomic.AtomicUtil;
import com.sinovoice.hcicloud.nettysocketiofirst.common.atomic.ConcurrentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class LoginHandler {

    //添加connect事件
    @OnConnect
    public void onConnect(SocketIOClient client) {
        try {
            String remote = client.getRemoteAddress().toString();
            UUID sessionId = client.getSessionId();

            Map<?, ?> params = client.getHandshakeData().getUrlParams();
            //获取客户端连接的uuid参数
            Object object = params.get("ctiCode");
            String ctiCode = "";
            if (object != null) {
                //现在使用 concurrenthashmap 保存对应关系
                ctiCode = ((List<String>) object).get(0).replaceAll("[a-zA-Z]", "");
                ConcurrentUtil.put(ctiCode, sessionId);

                //给客户端发送消息
                client.sendEvent("connect_msg", remote + "客户端你好，我是服务端");
            }
            log.info("当前用户sessionId为：{}，远程地址为：{}， 登录成功。当前连接数为：{}", sessionId, remote, AtomicUtil.add());
            log.info("当前hashmap的大小为： {}", ConcurrentUtil.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
