package com.sinovoice.hcicloud.nettysocketiofirst.config;

import com.corundumstudio.socketio.Configuration;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Sharable
public class CustomWrongUrlHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    Configuration configuration = null;

    /**
     * @param configuration
     */
    public CustomWrongUrlHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        ByteBuf buf = (ByteBuf)msg;
//        byte[] req1 = new byte[buf.readableBytes()];
//        //将buf中的数据读取到req中
//        buf.readBytes(req1);
//        String body = new String(req1, "UTF-8").substring(0, req1.length - 1);
//        log.info("CustomWrongUrlHandler接受到的字符为-001：" + body);
//
//
//        ByteBuf buffer=(ByteBuf) msg;
//        while (buffer.isReadable()) {
//            log.info("CustomWrongUrlHandler接受到的字符为-002：" + (char)buffer.readByte());
//        }
//
//        String body1 = (String)msg;
//        log.info("CustomWrongUrlHandler接受到的字符为-003：" + body1);
//
//
//
//        if (msg instanceof FullHttpRequest) {
//            FullHttpRequest req = (FullHttpRequest) msg;
//
//            Channel channel = ctx.channel();
//            QueryStringDecoder queryDecoder = new QueryStringDecoder(req.getUri());
//
//            // Don't log when port is pinged for monitoring. Must use context that starts with /ping.
//            if (configuration.isAllowCustomRequests() && queryDecoder.path().startsWith("/ping")) {
//                HttpResponse res = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
//                channel.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
//                req.release();
//                //log.info("Blocked wrong request! url: {}, ip: {}", queryDecoder.path(), channel.remoteAddress());
//                return;
//            }
//
//            // This is the last channel handler in the pipe so if it is not ping then log warning.
////            HttpResponse res = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
////            ChannelFuture f = channel.writeAndFlush(res);
////            f.addListener(ChannelFutureListener.CLOSE);
////            req.release();
//
//            log.warn("004-------Blocked wrong socket.io-context request! url: {}, params: {}, ip: {}", new Object[]{queryDecoder.path(), queryDecoder.parameters(), channel.remoteAddress()});
//        }
    }
}
