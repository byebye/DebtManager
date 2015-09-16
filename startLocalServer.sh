HOST=localhost
URL=http://$HOST
JDK=/usr/lib/jvm/java-8-jdk/bin
JAVA=$JDK/java
RMIREGISTRY=$JDK/rmiregistry
VERSION=0.0.2
CODEBASE=$PWD/common/build/libs/common-$VERSION.jar
SERVER=$PWD/server/build/libs/server-all-$VERSION.jar

killall rmiregistry 2> /dev/null

sleep 1
echo "Starting registry"
$RMIREGISTRY -J-Djava.rmi.server.codebase=file:///$CODEBASE -J-Djava.rmi.server.useCodebaseOnly=false -J-Djava.security.policy=Allpermissions.policy &
echo "Registry started"

sleep 1
$JAVA -Djava.security.policy=Allpermissions.policy \
      -Djava.rmi.server.codebase=file:///$CODEBASE \
      -Djava.rmi.server.hostname=$HOST \
      -jar $SERVER -u debtmanager -p debtmanager -d localhost/debtmanager &

