HOST=michalglapa.student.tcs.uj.edu.pl
URL=http://$HOST
JDK=~/jdk1.8.0_45/bin
JAVA=$JDK/java
RMIREGISTRY=$JDK/rmiregistry
JAR=./server_release/target/server-all-jar.jar


cp common/target/common*.jar ~/public_html/classes/common.jar

killall rmiregistry

sleep 1
$RMIREGISTRY -J-Djava.rmi.server.codebase=$URL/classes/common.jar  -J-Djava.rmi.server.useCodebaseOnly=false -J-Djava.security.policy=Allpermissions.policy &

echo "Registry started"
$JAVA   -Djava.security.policy=Allpermissions.policy \
        -Djava.rmi.server.codebase=$URL/classes/common.jar \
        -Djava.rmi.server.hostname=$HOST \
        -jar $JAR  -u debtmanager -p debtmanager -d localhost/debtmanager &


