HOST=managedebtsfor.me
URL=http://$HOST
JDK=/usr/bin
JAVA=$JDK/java
VERSION=0.0.1
CODEBASE=$PWD/common/target/common-$VERSION.jar

$JAVA -Djava.security.policy=Allpermissions.policy \
      -Djava.rmi.server.codebase=$URL/classes/common.jar \
      -Djava.rmi.server.hostname=$HOST \
      -jar ./client_release/target/client-all-jar.jar --host=$HOST
