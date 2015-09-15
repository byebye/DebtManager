# DebtManager
Application which solves the problem of whip-rounds: who owes whom and how much

##Using client
It is currently hosted on http://managedebtsfor.me

One can download an .jnlp file from there and use Java Web Start to start a client.
As we do not possess a code signing certificate you should add http://managedebtsfor.me to exception list in Java Control Panel
in order to be able to run it. That is enough if you want to just use the app.

##Building
However, if you want to build it yourself keep in mind that it is heavily customized for our setup
(Amazon EC2 instance running Linux, Amazon RDS running PostgreSQL and SendGrid for sending emails).


**Requirements:**

1. Java JDK 8 with JavaFX included
1. Maven - to build and download needed libraries:
   * jOOQ 3.5.4 (only server)
   * PostgreSQL JDBC Driver (only server)
   * common (internal library used by both client and server)
   * and more

To build

    mvn package


**Opening in IDE:**

To open project in IDE import it as a maven project. 
You may need to configure IDE to use Java 8 version.


###Client with GUI

**Executing from the command line**

*Will appear soon...*

###Server

**PostgreSQL preparation:**

Run `psql` as user with rights to create new users and databases.

1. Create user *debtmanager* with password *debtmanager* (you can change it later) and associated database:
   
    ```psql
    CREATE ROLE debtmanager WITH PASSWORD 'debtmanager' CREATEDB LOGIN;
    CREATE DATABASE debtmanager;
    GRANT ALL PRIVILEGES ON DATABASE debtmanager to debtmanager;
    ```
   
1. Execute in console the SQL script to create needed schemas and tables:

    `$ psql -U debtmanager -h localhost < server/src/main/resources/CreateTables.sql`

**Configuration**

*Will appear soon...*

**Executing from the command line**

*Will appear soon...*



