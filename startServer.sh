HOST=ec2-52-24-181-183.us-west-2.compute.amazonaws.com
URL=http://$HOST
JAVA=java
RMIREGISTRY=rmiregistry
JAR=./server_release/target/server-all-jar.jar



git pull

export JAVA_1_8_HOME=/usr
mvn package
cp common/target/common*.jar ~/WWW/classes/common.jar
cp client_release/target/client-all-jar.jar ~/WWW/classes/client-all-jar.jar
cp client.jnlp ~/WWW/classes/client.jnlp
jarsigner -keystore ~/debtmanagerKeystore ~/WWW/classses/client-all-jar.jar  debtmanagerKey


killall java
killall rmiregistry

sleep 1
$RMIREGISTRY -J-Djava.rmi.server.codebase=$URL/classes/common.jar  -J-Djava.rmi.server.useCodebaseOnly=false -J-Djava.security.policy=Allpermissions.policy &

echo "Registry started"
$JAVA   -Djava.security.policy=Allpermissions.policy \
        -Djava.rmi.server.codebase=$URL/classes/common.jar \
        -Djava.rmi.server.hostname=$HOST \
        -jar $JAR  -u debtmanager -p debtmanager -d debtmanagerdb2.cl0gzsk409pj.us-west-2.rds.amazonaws.com/debtmanager > server_log 2>&1 &


