# DebtManager
Application which solves the problem of whip-rounds: who owes whom and how much.

##Using client
It is currently hosted on http://managedebtsfor.me

One can download an .jnlp file from there and use Java Web Start to start a client.
As we do not possess a code signing certificate you should add http://managedebtsfor.me to exception list in Java Control Panel
in order to be able to run it. That is enough if you want to just use the app.

##Building
However, if you want to build it yourself keep in mind that it is heavily customized for our setup 
(Amazon EC2 instance running Linux, Amazon RDS running PostgreSQL and SendGrid for sending emails).


####Prerequisites

1. Java JDK 8 with JavaFX included
1. Gradle - to download needed libraries and build application
1. PostgreSQL - required when you want to run server (see [PostgreSQL configuration](#postgresql-configuration)

####Gradle
DebtManager uses Gradle as a build system:

  - `gradle -q tasks` - prints available tasks with description
  - `gradle <task>` - execute `<task>` for whole project
  - `gradle <task> -p <project-dir>` - execute `<task>` for application in specified directory, e.g. `client/` or `server/`

Use task `shadowJar` to build standalone (with all dependencies inside) `jar` package which will be located under `<project>/build/libs/`, for example

```bash
$ gradle shadowJar -p client/
$ ls client/build/libs/
client-all-0.0.2.jar
```

If you want to know how to run specific `jar`, look into `start.sh` script.

####Opening in IDE

If you use IntelliJ or Eclipse, run `gradle idea` or `gradle eclipse` respectively and open project.
Otherwise import it as a gradle project. 
You may need to configure IDE to use Java 8 version.


####Executing from the command line

There is script `start.sh` which serves to start client and server (on Linux)

```bash
chmod +x start.sh     # make script executable
./start.sh -h         # print help
./start.sh -bsc       # [b]uild, run [s]erver and [c]lient
```

Inside this file you can find some options to adjust execution of application - they are rather self-descriptive.
Default configuration should be sufficient to run both client and server on your computer.

###Server

To start server you must have PostgreSQL installed and configured.

####PostgreSQL configuration

Run `psql` as user with rights to create new users and databases.

1. Create user *debtmanager* with password *debtmanager* (you can change it later) and associated database:
   
    ```psql
    CREATE ROLE debtmanager WITH PASSWORD 'debtmanager' CREATEDB LOGIN;
    CREATE DATABASE debtmanager;
    GRANT ALL PRIVILEGES ON DATABASE debtmanager to debtmanager;
    ```
   
1. Execute in console the SQL script to create needed schemas and tables:

    `$ psql -U debtmanager -h localhost < server/src/main/resources/CreateTables.sql`

