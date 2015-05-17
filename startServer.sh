HOST=michalglapa.student.tcs.uj.edu.pl
URL=http://$HOST
JDK=~/jdk1.8.0_45/bin
JAVA=$JDK/java
RMIREGISTRY=$JDK/rmiregistry


cp common/target/common*.jar ~/public_html/classes/common.jar
killall rmiregistry

sleep 1
$RMIREGISTRY -J-Djava.rmi.server.codebase=$URL/classes/  -J-Djava.rmi.server.useCodebaseOnly=false &

echo "Registry started"
$JAVA -cp server/target/server-0.0.1.jar:common/target/common-0.0.1.jar:/home/z1111813/.m2/repository/org/jooq/jooq/3.5.4/jooq-3.5.4.jar:/home/z1111813/.m2/repository:/home/z1111813/.m2/repository/org/postgresql/postgresql/9.4-1201-jdbc41/postgresql-9.4-1201-jdbc41.jar  \
        -Djava.security.policy=Allpermissions.policy \
         -Djava.rmi.server.codebase=$URL/classes/common.jar \
         -Djava.rmi.server.hostname=$HOST \
         server.Server


