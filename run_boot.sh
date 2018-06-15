# description: Auto-starts boot

Tag="NettySocketApplication2.0"
App="netty-socketio-first-2.0.jar"
#MainClass="com.sinovoice.hcicloud.nettysocketiofirst.NettySocketioFirstApplication"
#Lib="/test/lib/"
#使用springBoot的日志框架文件配置中的路径(/home/nettysocket/data/aicc/logs/foreignserver/log),需要新建好文件夹 
#所以这里指定到/dev/null，但是这样子日志莫名奇妙的少了，就改成指定到固定文件 
#Log="/home/nettysocket/data/aicc/logs/foreignserver/log/foreignserver-2018-05-30-0.log"
echo $Tag
RETVAL="0"

# See how we were called.
function start() {
    echo  $Log 
    if [ ! -f $Log ]; then
        touch $Log
    fi
   #nohup java -jar $App -Dappliction=$Tag  > $Log 2>&1 &  
    nohup java -Xms2048m -Xmx5120m -Xss512K -XX:PermSize=512m -XX:MaxPermSize=1024m -jar $App -Dappliction=$Tag  2>&1 &
   #tailf $Log
    tailf nohup.out
}


function stop() {
    pid=$(ps -ef | grep -v 'grep' | egrep $Tag| awk '{printf $2 " "}')
    if [ "$pid" != "" ]; then      
        echo -n "boot ( pid $pid) is running" 
        echo 
        echo -n $"Shutting down boot: "
        pid=$(ps -ef | grep -v 'grep' | egrep $Tag| awk '{printf $2 " "}')
        if [ "$pid" != "" ]; then
            echo "kill boot process"
            kill -9 "$pid"
        fi
        else 
             echo "boot is stopped" 
        fi

    status
}

function status()
{
    pid=$(ps -ef | grep -v 'grep' | egrep $Tag| awk '{printf $2 " "}')
    #echo "$pid"
    if [ "$pid" != "" ]; then
        echo "boot is running,pid is $pid"
    else
        echo "boot is stopped"
    fi
}



function usage()
{
   echo "Usage: $0 {start|stop|restart|status}"
   RETVAL="2"
}

# See how we were called.
RETVAL="0"
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    reload)
        RETVAL="3"
        ;;
    status)
        status
        ;;
    *)
      usage
      ;;
esac

exit $RETVAL
