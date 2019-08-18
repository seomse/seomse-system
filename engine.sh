#/bin/sh

#Writer : wini(yskim)
#Date : 2017.10

#shich java -  java path Confirmation ex: opt/java/
export JAVA_HOME=
#HOME DIRECTORY
export APP_HOME=/opt/moara/engine

#CLASSPATH SET
#export CLASSPATH=${APP_HOME}/resources
export CLASSPATH=${APP_HOME}/classes


for filename in ${APP_HOME}/lib/*.jar
do
    CLASSPATH=${CLASSPATH}:$filename 
done

#for filename in ${APP_HOME}/lib_drill/*.jar
#do
#    CLASSPATH=${CLASSPATH}:$filename
#done

#CONSOL LOG PATH
export CONSOLE_LOG=log/$(date +%Y-%m-%d)_ENGINE_${2}_CONSOLE.log

# JAVA_OPTIONS FOR PROCESS IDENTIFY
export JAVA_OPTION=MOARA_ENGINE_${2}

cd ${APP_HOME}

case "${1}" in
start)
	 export pid=`ps -ef | grep ${JAVA_OPTION} | grep -v 'grep' | awk '{print $2}'`
   if [ -z ${pid} ]; then
   	nohup ${JAVA_HOME}java -D${JAVA_OPTION} -Xms${4}m -Xmx${5}m -cp ${CLASSPATH}  org.moara.engine.MoaraEngine ${2} ${3} > ${CONSOLE_LOG} &
	  echo 'engine start' 
	 else
  	echo 'Being already engine' 
   fi

	;;
kill)   
	export pid=`ps -ef | grep ${JAVA_OPTION} | grep -v 'grep' | awk '{print $2}'`
         if [ -z ${pid} ]; then
          echo 'kill fail: engine is not start'
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
