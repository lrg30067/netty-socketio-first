package com.sinovoice.hcicloud.nettysocketiofirst.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;

public class CustomSocketIOChannelInitializer extends SocketIOChannelInitializer {
    CustomWrongUrlHandler customWrongUrlHandler = null;

    public CustomSocketIOChannelInitializer(Configuration configuration) {
        customWrongUrlHandler = new CustomWrongUrlHandler(configuration);
    }

    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        addSslHandler(pipeline);
        addSocketioHandlers(pipeline);
        // Replace wrong url handler with our custom one to allow network monitoring without logging warnings.
        pipeline.replace(SocketIOChannelInitializer.WRONG_URL_HANDLER, "wrongUrlBlocker", customWrongUrlHandler);
    }
}
