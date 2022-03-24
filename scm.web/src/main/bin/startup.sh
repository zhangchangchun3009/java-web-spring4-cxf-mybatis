#!/bin/bash
if [ -z "$JAVA_HOME" ]; then
  echo "JAVA_HOME is not set."
  exit 1
fi
current_dir=`pwd`
cd ..
lib_dir=`pwd`/lib
classpath="."
for f in ${lib_dir}/*.jar; do
  classpath=${classpath}:$f;
done
echo classpath:============== 
echo $classpath
log_dir=`pwd`/logs
if [ ! -d "$log_dir" ] ; then
mkdir logs
touch logs/scm.out
fi
main_class=pers.zcc.scm.web.launch.AppBootstrap
java_opts="-Dspring.profiles.active=dev -Dfile.encoding=UTF-8 -Xms1024m -Xmx1024m"
echo =====================
echo log directory: $log_dir
echo =====================
echo java options: $java_opts
nohup ${JAVA_HOME}/bin/java $java_opts -classpath "$classpath" $main_class > ${log_dir}/scm.out 2>&1 < /dev/null &
