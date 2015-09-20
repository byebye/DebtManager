#!/bin/bash

host=localhost
server_url=http://${host}
jdk_bin=/usr/lib/jvm/java-8-jdk/bin
java=${jdk_bin}/java
rmi=${jdk_bin}/rmiregistry
app_version=0.0.3
codebase=${PWD}/common/build/libs/common-all-${app_version}.jar
server=${PWD}/server/build/libs/server-all-${app_version}.jar
client=${PWD}/client/build/libs/client-all-${app_version}.jar

function print_help() {
   echo -e "Options:\n"\
   "-h  display this help\n"\
   "-b  build the whole project (using gradle)\n"\
   "-c  start client\n"\
   "-s  start server\n"\
   "-t  terminate running server\n"\
   "Info: for more settings modify this script."
}

function build() {
   gradle shadowJar -p common/
   gradle shadowJar -p server/ 
   gradle shadowJar -p client/
}

function start_client() {
   echo "Starting client..."
   echo "Connecting to ${host}"

   ${java}  -Djava.security.policy=Allpermissions.policy \
            -Djava.rmi.server.codebase=${server_url}/classes/common.jar \
            -Djava.rmi.server.hostname=${host} \
            -jar ${client} --host=${host} &
}

function stop_server() {
   echo "Terminating running server.."   
   ps au | grep -- "-[j]ar ${server}" | awk '{ print $2 }' | xargs kill -9 2> /dev/null
   killall rmiregistry 2> /dev/null
}

function start_rmi() {
   echo "Starting RMI registry"
   ${rmi}   -J-Djava.rmi.server.codebase=file:///${codebase} \
            -J-Djava.rmi.server.useCodebaseOnly=false \
            -J-Djava.security.policy=Allpermissions.policy &
   echo "RMI started"
}

function start_server() {
   echo "Starting server..."
   stop_server
   sleep 1
   start_rmi
   sleep 1
   ${java}  -Djava.security.policy=Allpermissions.policy \
            -Djava.rmi.server.codebase=file:///${codebase} \
            -Djava.rmi.server.hostname=${host} \
            -jar ${server} -u debtmanager -p debtmanager -d localhost/debtmanager &
}

if [ -z "${1}" ]; then
   print_help && exit 0
fi
while getopts ":bcst" option; do
   case ${option} in
      h) print_help && exit 0 ;;
      b) build  ;;
      c) start_client=true ;;
      s) start_server=true ;;
      t) stop_server && exit 0 ;;
      \?)  echo "Unknown option specified: -${OPTARG}" >&2 && print_help && exit 1 ;;
   esac
done

if [ ${start_server} ]; then
   start_server
fi
if [ ${start_client} ]; then
   start_client
fi
