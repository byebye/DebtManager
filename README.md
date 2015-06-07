# DebtManager
Application which solves the problem of whip-rounds: who owes whom and how much

##Using client
It is currently hosted on http://managedebtsfor.me

One can download an .jnlp file from there and use Java Web Start to start a client.
As we do not posess a code signing certificate you should add http://managedebtsfor.me to exception list in Java Control Panel
in order to be able to run it. That is enough if you want to just use the app.

##Building
However, if you want to build it yourself keep in mind that it is heavily customized for our setup
(Amazon EC2 instance running linux and Amazon RDS running PostgreSQL and SendGrid for sending emails).


**Requirements:**

1. Java JDK 8 with JavaFX included
3. Maven - to build and download needed libraries:
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

1. Create user "*debtmanager*" with password "*debtmanager*" (you can change it later) and with permission to create new databases:
   
   `> CREATE ROLE debtmanager  PASSWORD debtmanager CREATEDB LOGIN`

2. Execute the SQL script to create needed schemas and tables:

   `$ psql -U debtmanager < server/src/main/resources/create_database.sql`

**Configuration**

*Will appear soon...*

**Executing from the command line**

*Will appear soon...*



