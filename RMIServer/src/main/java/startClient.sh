HOST=michalglapa.student.tcs.uj.edu.pl
URL=http://$HOST
JAVA=java
$JAVA -Djava.security.policy=AllPermissions.policy \
         client.MessageBoxClient $HOST



