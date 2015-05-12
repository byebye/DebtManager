# DebtManager
Application which solves the problem of whip-rounds: who owes whom and how much.

##Building

**Requirements:**

1. Java JDK 8 with JavaFX included
2. PostgreSQL 9.4 (required only to build server)
3. Maven - to build and download needed libraries:
   * jOOQ 3.5.4 (only server)
   * PostgreSQL JDBC Driver (only server)
   * common (internal library used by both client and server)

**Adding 'common' package to Maven repository:**

In order to execute client or server you need to build a 'common' library and add it to your local maven repository.
To do this, get into *common* folder and type:
```
$ mvn package
$ mvn install:install-file 
    -Dfile='target/common-0.0.1.jar' 
    -DgroupId=tcs.javaproject 
    -DartifactId=common 
    -Dversion=0.0.1 
    -Dpackaging=jar
```
Now maven should be able to resolve all required dependencies (other libraries will be downloaded from online repositories).

**Opening in IDE:**

To open project in IDE import it as a maven project. 
You may need to configure IDE to use Java 8 version.

###Client with GUI

**Executing from the command line**

*Will appear soon...*

###Server

**PostgreSQL preparation:**

1. Create user "*debtmanager*" with password "*debtmanager*" (you can change it later) and with permission to create new databases:
   
   `> CREATE ROLE debtmanager  PASSWORD debtmanager CREATEDB LOGIN`

2. Execute the SQL script to create needed schemas and tables:

   `$ psql -U debtmanager < server/src/main/resources/create_database.sql`

**Configuration**

*Will appear soon...*

**Executing from the command line**

*Will appear soon...*


###RMIServer

**Running client**

````
$ java -Djava.security.policy=[ABSOULUTE_PATH]/DebtManager/RMIServer/src/main/java/AllPermissions.policy 
       client.MessageBoxClient [hostname] (localhost or michalglapa.student.tcs.uj.edu.pl)
````

**Running server LOCALLY**
    
````
$ java -Djava.security.policy=[ABSOLUTE_PATH]/DebtManager/RMIServer/src/main/java/AllPermissions.policy 
       -Djava.rmi.server.codebase=file://[ABS_PATH]/DebtManager/RMIServer/ server.RMIServer
````

**Running server on my users**

`make runServer`

Repeat if does not work

