package com.sinovoice.hcicloud.nettysocketiofirst.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.sinovoice.hcicloud.nettysocketiofirst.listener.RedisMsgPubSubListener;
import com.sinovoice.hcicloud.nettysocketiofirst.thread.SubscribeProThread;
import com.sinovoice.hcicloud.nettysocketiofirst.thread.SubscribeThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
@Configuration
public class NoticeEventHandler {

    private static final Logger log = LoggerFactory.getLogger(NoticeEventHandler.class);
    private final SocketIOServer server;

    private static ExecutorService exec = Executors.newCachedThreadPool();
//    private static ExecutorService exec = Executors.newFixedThreadPool(2000);


    @Value("${netty_server_tip_url}")
    private String tipUrl;


    public static Map<String, SocketIOClient> clientsMap = new ConcurrentHashMap<String, SocketIOClient>();
    public static Map<String, RedisMsgPubSubListener> clientCtiCodeMap = new ConcurrentHashMap<String, RedisMsgPubSubListener>();
    public static Map<String, FutureTask<Integer>> threadMap002 = new ConcurrentHashMap<String, FutureTask<Integer>>();

    public static int num = 0;


    @Autowired
    public NoticeEventHandler(SocketIOServer server) {
        this.server = server;
    }

    //添加connect事件，当客户端发起连接时调用
    @OnConnect
    public void onConnect(SocketIOClient client) {
        try {
            String sa = client.getRemoteAddress().toString();
            String sessionId = client.getSessionId().toString();

            log.info(String.format("登录数统计, sessionId %s, num %s", sessionId, ++num));

            SocketIOClient clientMap = clientsMap.get(sessionId);
            if (clientMap != null) {
                FutureTask<Integer> future = threadMap002.get(sessionId);
                this.onDisconnect(clientMap);

                log.info(String.format("sessionId %s 重复登录, 当前的连接个数为： num %s, redis连接个数为： num %s, " +
                        "redis线程数为： num %s", sessionId, clientsMap.size(), clientCtiCodeMap.size(), threadMap002.size()));
            } else {
                log.info(sa + "-------------------------" + "客户端已连接");
                log.info(String.format("SocketIOClient 统计, sessionId %s, num %s", sessionId, clientsMap.size() + 1));

                Map<?, ?> params = client.getHandshakeData().getUrlParams();
                //获取客户端连接的uuid参数
                Object object = params.get("ctiCode");
                String ctiCode = "";
                if (object != null) {
                    ctiCode = ((List<String>) object).get(0);
                    //将uuid和连接客户端对象进行绑定
                    clientsMap.put(sessionId, client);

                    //给客户端发送消息
                    client.sendEvent("connect_msg", sa + "客户端你好，我是服务端");

                    SubscribeProThread subscribeProThread = new SubscribeProThread(sessionId, ctiCode, tipUrl);
                    FutureTask<Integer> future = new FutureTask<Integer>(subscribeProThread);
                    exec.submit(future);


//                System.out.println("线程是否结束001：" + future.isDone());
//                System.out.println("线程是否被取消001：" + future.isCancelled());
                    threadMap002.put(sessionId, future);
                    log.info(String.format("sessionId %s 上线了, 当前的连接个数为： num %s, redis连接个数为： num %s, " +
                            "redis线程数为： num %s", sessionId, clientsMap.size(), clientCtiCodeMap.size(), threadMap002.size()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        try {
            String sessionId = client.getSessionId().toString();
            clientsMap.remove(sessionId);

            //因为future.cancel(true)，不能中断redis的订阅监听。所以先取消订阅，在释放线程资源
            if (clientCtiCodeMap.get(sessionId) != null) {
                RedisMsgPubSubListener redisListner = clientCtiCodeMap.get(sessionId);
                redisListner.unsubscribe();
                clientCtiCodeMap.remove(sessionId);
                log.info(String.format("sessionId %s redis is 取消订阅." , sessionId));
            }

            if (threadMap002.get(sessionId) != null) {
                Future<?> future = threadMap002.get(sessionId);
                future.cancel(true);
//                System.out.println("线程是否结束002：" + future.isDone());
//                System.out.println("线程是否被取消002：" + future.isCancelled());
                threadMap002.remove(sessionId);
                log.info(String.format("sessionId %s is Interrupting." , sessionId));
                log.info(String.format("sessionId %s 下线了, 当前的连接个数为： num %s, redis连接个数为： num %s, " +
                        "redis线程数为： num %s", sessionId, clientsMap.size(), clientCtiCodeMap.size(), threadMap002.size()));
            }
        } catch (Exception e) {
            log.error("SocketIO server onDisconnect error: {}!", e);
        }
    }
}
