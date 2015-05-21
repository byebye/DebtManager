HOST=localhost
URL=http://$HOST
JDK=/usr/lib/jvm/java-8-jdk/bin
JAVA=$JDK/java
VERSION=0.0.1
CODEBASE=$PWD/common/target/common-$VERSION.jar

$JAVA -cp client/target/client-$VERSION.jar:common/target/common-$VERSION.jar \
      -Djava.security.policy=Allpermissions.policy \
      -Djava.rmi.server.codebase=file:///$CODEBASE \
      -Djava.rmi.server.hostname=$HOST \
      client.windows.LoginWindow
