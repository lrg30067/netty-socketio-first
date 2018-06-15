package com.sinovoice.hcicloud.nettysocketiofirst.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Notice;

public class ContentListener implements DataListener<Notice> {

    SocketIOServer server;

    public void setServer(SocketIOServer server) {
        this.server = server;
    }

    SocketIOClient client;

    public void setClient(SocketIOClient client) {
        this.client = client;
    }

    @Override
    public void onData(SocketIOClient socketIOClient, Notice notice, AckRequest ackRequest) throws Exception {
        //服务端接受客户端的消息后，回应客户端， chatevent为 事件的名称， msg为发送的内容
        this.server.getBroadcastOperations().sendEvent("notice_event", notice);

//        client.sendEvent("notice_event", notice);
    }
}
