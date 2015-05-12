URL=http://michalglapa.student.tcs.uj.edu.pl
killall rmiregistry
rmiregistry -J-Djava.rmi.server.codebase=$URL/classes/ &

echo "Registry started"
java -Djava.security.policy=AllPermissions.policy \
         -Djava.rmi.server.codebase=$URL/classes/ \
         -Djava.rmi.server.hostname=$URL \
         server.RMIServer



