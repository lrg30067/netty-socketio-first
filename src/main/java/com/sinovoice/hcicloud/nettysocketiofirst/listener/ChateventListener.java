package com.sinovoice.hcicloud.nettysocketiofirst.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Msg;

public class ChateventListener implements DataListener<Msg> {

    SocketIOServer server;

    public void setServer(SocketIOServer server) {
        this.server = server;
    }

    public void onData(SocketIOClient socketIoClient, Msg msg,
                       AckRequest askSender) throws Exception {

        //服务端接受客户端的消息后，回应客户端， chatevent为 事件的名称， msg为发送的内容
        this.server.getBroadcastOperations().sendEvent("chatevent", msg);

    }

}
