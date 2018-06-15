# netty-socketio-first

标签（空格分隔）： socket.io netty-socketio java springboot 

---


![目录.png](https://upload-images.jianshu.io/upload_images/539247-ad61405ef25c67f5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


##背景：对话消息实时提醒功能开发

##详细设计文本描述：
- 使用springBoot启动，容器启动完成后（使用CommandLineRunner或ApplicationRunner接口），启动netty_websocket服务。
- 客户端登录上线，在OnConnect监听器事件中，线程池中使用Future的形式，执行订阅任务，订阅任务线程阻塞，connect线程正常执行。
- 关闭浏览器、直接关闭客户端程序、kill进程、主动执行disconnect方法都会导致立刻产生断线事件，在OnDisconnect监听器事件中，取消订阅，释放订阅任务线程资源。

##架构图：
![架构图.png](https://upload-images.jianshu.io/upload_images/539247-18373e8c23c3b046.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##业务流程图：
![业务流程图.png](https://upload-images.jianshu.io/upload_images/539247-4845618515c1db44.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##相关准备
- 1.网站的运行环境
netty-socketio  `1.7.12`
socket.io  `2.1.1`
java   `1.8`
nginx   `1.3+`

- 2.相关配置描述（这里选择的是正式上的配置）
![application-proshw.png](https://upload-images.jianshu.io/upload_images/539247-31a6ae2447a1b1d0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


- 3.对参数的理解，大部分参考博客相关文档，如有问题，欢迎指正。


`spring.server.maxThreads=5000`
tomcat中支持的最大线程数，因为websocket服务器在springboot中启动，redis的每一次订阅都会阻塞，每个客户端就至少产生一个线程，于是，就会设想连接数可能会受限于tomcat的线程数。具体实际测试结果，一会下文给出。

`netty_server_boss_count=12`
`netty_server_work_count=200`
参考：
[netty实战之百万级流量NioEventLoopGroup线程数配置](https://blog.csdn.net/linsongbin1/article/details/77698479)指出：
如果你的应用服务的QPS只是几百万,那么parentGroup只需要设置为2,childGroup设置为4

netty_server_boss_count的默认值为：服务器的物理核数
netty_server_work_count的默认值不确定，但是网上一般设置100。
具体改为2，4的设置，目前还没有经过测试，有待验证。

- 4.nginx.conf的部分配置
```conf
upstream io_nodes {
    ip_hash; # 实际需求肯定采用这个策略，但是本地压测使用的都是同一个 ip，将会导致请求只分配到同一个服务器上
    server 10.0.0.222:7903;
    server 10.0.1.227:7905;
    server 10.0.2.82:7905;
}

server {
    listen 7903;
    server_name server.com;

    error_log  logs/error.log debug;
    access_log  logs/access.log;

    location / {
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header Host $http_host;
        proxy_set_header X-NginX-Proxy true;

        proxy_pass http://io_nodes;
        proxy_redirect off;

        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}

```

- 5.启动脚本run_boot.sh一部分
```sh
Tag="NettySocketApplication2.0"
App="netty-socketio-first-2.0.jar"
#MainClass="com.sinovoice.hcicloud.nettysocketiofirst.NettySocketioFirstApplication"
#Lib="/test/lib/"
#使用springBoot的日志框架文件配置中的路径(/home/nettysocket/data/aicc/logs/foreignserver/log),需要新建好文件夹 
#所以这里指定到/dev/null，但是这样子日志莫名奇妙的少了，就改成指定到固定文件 
#Log="/home/nettysocket/data/aicc/logs/foreignserver/log/foreignserver-2018-05-30-0.log"
echo $Tag
RETVAL="0"
```
`./run_boot.sh start`
`./run_boot.sh stop`
`./run_boot.sh restart`
`./run_boot.sh status`

备注：可以自行修改APP为实际名称

##测试结果
这里使用网上推荐的`websocket-bench`进行测试。可以参考
[使用websocket-bench进行socket.io性能测试](https://blog.csdn.net/ljc82/article/details/78076106)

- 具体测试命令：
`websocket-bench -a 2000 -c 1 -w 4 -k  http://10.0.1.227:7903?ctiCode=106 -o opt.log`

- 测试结果opt.log打印
![opt.log.png](https://upload-images.jianshu.io/upload_images/539247-4c7b83ba390fb46e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 参数简单描述：
-a:一共的连接数
-c:每秒的请求数
-w:几个工作线程执行，这里我设置为测试服务器的物理核数
-k:是否保持长连接

- 遇到的问题描述：

1. 改为类似的测试语句
`websocket-bench -a 2000 -c 2 -w 8 -k  http://127.0.1.227:7903?ctiCode=106 -o opt.log -v`
最后查看redis的连接数，始终达不到2000，有些连接被丢失，反复修改代码，反复测试（修改每秒的并发数），稳定的最大连接数始终维持在500，只要-a>500，就会出现丢失。基本定位问题是由于redis的订阅阻塞线程影响，但是没有合适解决方案。目前还没有正式上线，只能等线上正式环境（坐席最大连接数基本在1000左右）的考验，看怎么解决。

##预线上问题描述：
1.websocket不支持F5转发。[AuthorizedHandler Blocked wrong request!](https://github.com/mrniko/netty-socketio/issues/567)
2.浏览器提示跨域问题。因为前期也碰到过，但是单点连接就显示没有问题，可能nginx的配置有问题导致的。
> 没有成功的nginx配置，使用的nginx `1.11.1`,启用sticky模块的编译，以及使用负载均衡web查看状态工具，nginx_upstream_check_module，但是访问就一直报跨域的错误，就放弃这种配置了。具体原因不详。

```conf
upstream io_nodes {
    sticky;
    server 10.0.0.222:7903;
    server 10.0.1.227:7905;
    check interval=3000 rise=2 fall=5 timeout=1000 type=tcp;
}

server {
    listen 7903;
    server_name server.com;

    # 允许跨域
    #add_header Access-Control-Allow-Origin *;
    #add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
    #add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';
    #if ($request_method = 'OPTIONS') {
    #    return 204;
    #}


    error_log  logs/error.log debug;
    access_log  logs/access.log;


    location / {
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header Host $http_host;
        proxy_set_header X-NginX-Proxy true;

        proxy_pass http://io_nodes;
        proxy_redirect off;

        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";

    }
    location /nstatus {
        check_status;
        access_log off;
        #allow SOME.IP.ADD.RESS;
        #deny all;
    }
}

```





