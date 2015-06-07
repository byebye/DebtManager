HOST=localhost
URL=http://$HOST
JDK=/usr/lib/jvm/java-8-jdk/bin/ #~/jdk1.8.0_45/bin
JAVA=$JDK/java
RMIREGISTRY=$JDK/rmiregistry
VERSION=0.0.1
CODEBASE=$PWD/common/target/common-$VERSION.jar

killall rmiregistry

sleep 1
echo "Starting registry"
$RMIREGISTRY -J-Djava.rmi.server.codebase=file:///$CODEBASE -J-Djava.rmi.server.useCodebaseOnly=false -J-Djava.security.policy=Allpermissions.policy &

echo "Registry started"
$JAVA -Djava.security.policy=Allpermissions.policy \
      -Djava.rmi.server.codebase=file:///$CODEBASE \
      -Djava.rmi.server.hostname=$HOST \
      -jar ./server_release/target/server-all-jar.jar  -u debtmanager -p debtmanager -d localhost/debtmanager &

