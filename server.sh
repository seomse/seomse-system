#/bin/sh

#Writer : macle
#Date : 2019.10.24

#shich java -  java path Confirmation ex: opt/java/
export JAVA_HOME=
#HOME DIRECTORY
export APP_HOME=/opt/seomse/server

#Config path SET
export CONFIG_PATH=../config/seomse_config.xml

#Server id TB_SYSTEM_SERVER
export SERVER_ID=SEOMSE_SERVER


#CLASSPATH SET
#export CLASSPATH=${APP_HOME}/resources
export CLASSPATH=${APP_HOME}/classes

for filename in ${APP_HOME}/dependencies/*.jar
do
    CLASSPATH=${CLASSPATH}:$filename 
done


#CONSOL LOG PATH
export CONSOLE_LOG=log/$(date +%Y-%m-%d)_SEOMSE_SERVER_CONSOLE.log

# JAVA_OPTIONS FOR PROCESS IDENTIFY
export JAVA_OPTION=SEOMSE_SERVER

cd ${APP_HOME}

case "${1}" in
start)
	 export pid=`ps -ef | grep ${JAVA_OPTION} | grep -v 'grep' | awk '{print $2}'`
   if [ -z ${pid} ]; then
   	nohup ${JAVA_HOME}java -D${JAVA_OPTION} -Xms32m -Xmx1024m -cp ${CLASSPATH}  com.seomse.system.server.Server ${SERVER_ID} ${CONFIG_PATH} > ${CONSOLE_LOG} &
	  echo 'seomse server start'
	 else
  	echo 'Being already start' 
   fi

	;;
kill)   
	export pid=`ps -ef | grep ${JAVA_OPTION} | grep -v 'grep' | awk '{print $2}'`
         if [ -z ${pid} ]; then
          echo 'kill fail: server is not start'
         else
	  ps -ef | grep ${JAVA_OPTION} | grep -v grep | awk '{printf( "kill -TERM %s\n", $2); }' > controll/tmp.$$
          sh controll/tmp.$$
          rm -f controll/tmp.$$
	  echo 'stop success'
         fi 
	;;
*)
        echo "argument (start or kill)"
        ;;
esac
