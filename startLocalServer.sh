HOST=localhost
URL=http://$HOST
JDK=/usr/lib/jvm/java-8-jdk/bin #~/jdk1.8.0_45/bin
JAVA=$JDK/java
RMIREGISTRY=$JDK/rmiregistry
VERSION=0.0.1
CODEBASE=$PWD/common/target/common-$VERSION.jar

killall rmiregistry

sleep 1
echo "Starting registry"
$RMIREGISTRY -J-Djava.rmi.server.codebase=file:///$CODEBASE -J-Djava.rmi.server.useCodebaseOnly=false -J-Djava.security.policy=Allpermissions.policy &

echo "Registry started"
$JAVA -cp server/target/server-$VERSION.jar:common/target/common-$VERSION.jar:$HOME/.m2/repository/org/jooq/jooq/3.5.4/jooq-3.5.4.jar:$HOME/.m2/repository/org/postgresql/postgresql/9.4-1201-jdbc41/postgresql-9.4-1201-jdbc41.jar \
      -Djava.security.policy=Allpermissions.policy \
      -Djava.rmi.server.codebase=file:///$CODEBASE \
      -Djava.rmi.server.hostname=$HOST \
      server.Server -u debtmanager -p debtmanager -d localhost/debtmanager &

