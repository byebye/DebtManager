HOST=localhost
URL=http://$HOST
JDK=/usr/lib/jvm/java-8-oracle/bin
JAVA=$JDK/java
VERSION=0.0.1
CODEBASE=$PWD/common/target/common-$VERSION.jar

$JAVA -Djava.security.policy=Allpermissions.policy \
      -Djava.rmi.server.codebase=file:///$CODEBASE \
      -Djava.rmi.server.hostname=$HOST \
      -jar ./client_release/target/client-all-jar.jar --host="localhost"
