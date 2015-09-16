HOST=managedebtsfor.me
if [ "$1" != "" ]; then
   HOST="$1"
fi
URL=http://$HOST
JDK=/usr/bin
JAVA=$JDK/java
VERSION=0.0.2
CODEBASE=$PWD/common/libs/common-$VERSION.jar
CLIENT=$PWD/client/build/libs/client-all-$VERSION.jar

echo "Trying connect to host = $HOST"

$JAVA -Djava.security.policy=Allpermissions.policy \
      -Djava.rmi.server.codebase=$URL/classes/common.jar \
      -Djava.rmi.server.hostname=$HOST \
      -jar $CLIENT --host=$HOST
