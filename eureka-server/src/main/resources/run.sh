#!/bin/bash
extra=""
if [ -n $2 ];
then
    extra="--spring.profiles.active=$2"
fi

pname=eureka-server
COUNT=1
KILL_COUNT=11
proPid=/app/${pname}/bin/${pname}.pid
logDir=/app/${pname}/log
JAVA_BIN=java
JAVA_OPTS="-jar -Xms128m -Xmx512m"
JAR_PATH="/app/${pname}/bin/${pname}.jar"
JAVA_CONF="--spring.config.location=/app/${pname}/conf/application.yml  $extra"
JAVA_LOG=
cd /app/${pname}/bin

export LANG="en_US.UTF-8"
export LC_ALL="en_US.UTF-8"

function start()
{
    echo "Starting ${pname}: "
    echo "$JAVA_BIN $JAVA_OPTS $JAR_PATH $JAVA_CONF $JAVA_LOG > ${logDir}/start.log"
    nohup $JAVA_BIN $JAVA_OPTS $JAR_PATH $JAVA_CONF $JAVA_LOG >> /app/$pname/log/${pname}.out & echo $! > ${proPid}
    ps -ef |grep -v "grep"|grep "`cat ${proPid}`"
    if [ $? -eq 0 ];then
     echo "${pname} 启动成功"
    else
     echo "${pname} 启动失败"
    fi
}

function stop()
{
if [ -f ${proPid} ];then
        SPID=`cat ${proPid}`
        ps -ef |grep $SPID|grep -v grep > /dev/null
        proExist=$?
        while [[ $COUNT -lt $KILL_COUNT ]] && [[ $proExist -eq 0 ]]
        do
            echo "杀${COUNT}"
            kill $SPID
            sleep 3
            ps -ef |grep $SPID|grep -v grep > /dev/null
            proExist=$?
            let COUNT++
        done

        ps -ef |grep $SPID|grep -v grep > /dev/null
        if [ ! $? -eq 0 ];then
         echo > ${proPid}
         echo "${pname} 已经被干掉"
        else
         echo "${pname} 杀了${KILL_COUNT}次都杀不死."
         exit 1
        fi
else
  echo "${pname} pid文件不存在"
fi
}



function restart()
{
    echo "stoping ..."
    stop
    start
}

function installAndLink()
{
  echo "begin git pull"
  cd /usr/local/git/spring-cloud/$pname
  git pull
  echo "begin maven install"
  cd /usr/local/git/spring-cloud
  mvn clean install
  echo "finish install, begin soft link"
  formatDate=$(date "+%Y%m%d%H%M")
  target=$pname\_$formatDate.jar
  echo "link file $target"
  cd /app/$pname/bin
  mv /root/.m2/repository/com/test/perf/$pname/0.0.1-snapshot/$pname-0.0.1-snapshot.jar $target
  ln -sf $target ${pname}.jar
  echo "soft link success"
}


case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    install)
		installAndLink
	;;
    *)
        echo $"Usage: $0 {start|stop|restart}"
        RETVAL=1
esac
exit $RETVAL
