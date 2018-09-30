package com.sinovoice.hcicloud.nettysocketiofirst.config;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.sinovoice.hcicloud.nettysocketiofirst.listener.ChateventListener;
import com.sinovoice.hcicloud.nettysocketiofirst.vo.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettySocketConfig {

    @Value("${netty_server_ip}")
    private String serverIp;

    @Value("${netty_server_port}")
    private int port;

    @Value("${netty_server_boss_count}")
    private int bossCount;

    @Value("${netty_server_work_count}")
    private int workCount;

    @Value("${netty_server_allow_custom_requests}")
    private boolean allowCustomRequests;

    @Value("${netty_server_upgrade_timeout}")
    private int upgradeTimeout;


    @Value("${netty_server_ping_timeout}")
    private int pingTimeout;


    @Value("${netty_server_ping_interval}")
    private int pingInterval;


    private static final Logger log = LoggerFactory.getLogger(NettySocketConfig.class);

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

        config.setHostname(serverIp);
        config.setPort(port);

        //设置最大每帧处理数据的长度，防止他人利用大数据来攻击服务器
        config.setMaxFramePayloadLength(1024 * 1024);
        //设置http交互最大内容长度
        config.setMaxHttpContentLength(1024 * 1024);
        config.setBossThreads(bossCount);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);


        // 协议升级超时时间（毫秒），默认10秒。HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(upgradeTimeout);
        // Ping消息超时时间（毫秒），默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(pingTimeout);
        // Ping消息间隔（毫秒），默认25秒。客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(pingInterval);



        // 连接认证，这里使用token更合适
        config.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData data) {
                // String token = data.getSingleUrlParam("token");
                // String username = JWTUtil.getSocketUsername(token);
                // return JWTUtil.verifySocket(token, "secret");s
//                log.info(String.format("认证成功，远程地址为：%s", data.getAddress()));
                return true;
            }
        });

        SocketConfig socketConfig = new SocketConfig();
        //某个进程占用了80端口,然后重启进程,原来的socket1处于TIME-WAIT状态,
        //进程启动后,使用一个新的socket2,要占用80端口,如果这个时候不设置SO_REUSEADDR=true
        //,那么启动的过程中会报端口已被占用的异常。
        socketConfig.setReuseAddress(true);
        //服务是低延迟的
        socketConfig.setTcpNoDelay(true);
//		socketConfig.setSoLinger(0);
        //可以探测客户端的连接是否还存活着
        socketConfig.setTcpKeepAlive(true);



        config.setSocketConfig(socketConfig);
        final SocketIOServer server = new SocketIOServer(config);

        //添加实例监听器,全部客户端
        ChateventListener listener = new ChateventListener();
        listener.setServer(server);
        server.addEventListener("chatevent", Msg.class, listener);


        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}
