package tcs.javaproject.database;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseController {
   private final String dbUser = "debtmanager";
   private final String dbPassword = "debtmanager";
   private final String url = "jdbc:postgresql://localhost/debtmanager";
   private Connection connection;
   private DSLContext dbContext;

   public DatabaseController() {
      connect();
   }

   private void connect() {
      try {
         if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
            dbContext = DSL.using(connection, SQLDialect.POSTGRES);
         }
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public boolean validateUserPassword(String username, String password) {
      Result<Record1<String>> result =
              dbContext.select(Users.USERS.PASSWORD_HASH)
                       .from(Users.USERS)
                       .where(Users.USERS.NAME.equal(username)).fetch();
      if (result.isEmpty())
         return false;
      String expectedPassword = result.get(0).value1().trim();
      return password.equals(expectedPassword);
   }
}
