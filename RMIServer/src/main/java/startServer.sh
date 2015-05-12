HOST=michalglapa.student.tcs.uj.edu.pl
URL=http://$HOST
JAVA=java
killall rmiregistry
rmiregistry -J-Djava.rmi.server.codebase=$URL/classes/ &

echo "Registry started"
$JAVA -Djava.security.policy=AllPermissions.policy \
         -Djava.rmi.server.codebase=$URL/classes/ \
         -Djava.rmi.server.hostname=$HOST \
         server.RMIServer



