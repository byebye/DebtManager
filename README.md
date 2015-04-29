# DebtManager
Application which solves the problem of whip-rounds: who owes whom and how much.

##Building

**Requirements:**

1. Java JDK 8 with JavaFX included
2. PostgreSQL 9.4 (required only to build server)
3. Maven - will download needed libraries:
   * jOOQ 3.5.4 (only server)
   * PostgreSQL JDBC Driver (only server)

**Opening in IDE:**

To open project in IDE import it as a maven project. 
You may need to configure IDE to use Java 8 version.

###Client with GUI

**Executing from the command line**

*Will appear soon...*

###Server

**PostgreSQL preparation:**

1. Create user "*debtmanager*" with password "*debtmanager*" (you can change it later) and with permission to create new databases:
   
   `CREATE ROLE debtmanager  PASSWORD debtmanager CREATEDB LOGIN`

2. Execute the SQL script to create needed schemas and tables:

   `psql -U debtmanager < server/src/main/resources/create_database.sql`

**Configuration**

*Will appear soon...*

**Executing from the command line**

*Will appear soon...*