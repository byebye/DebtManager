package tcs.javaproject.database;

import org.jooq.*;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Budgets;
import tcs.javaproject.database.tables.UserBudget;
import tcs.javaproject.database.tables.Users;
import tcs.javaproject.database.tables.records.BudgetsRecord;
import tcs.javaproject.guitest.Budget;
import tcs.javaproject.guitest.User;

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
                       .where(Users.USERS.NAME.equal(username))
                       .fetch();
      if (result.isEmpty())
         return false;
      String expectedPassword = result.get(0).value1().trim();
      return password.equals(expectedPassword);
   }

   public User getUserByEmail(String email) {
      Result<Record2<Integer, String>> result =
              dbContext.select(Users.USERS.ID, Users.USERS.NAME)
                       .from(Users.USERS)
                       .where(Users.USERS.EMAIL.equal(email))
                       .fetch();
      if (result.isEmpty())
         return null;
      int id = result.get(0).value1();
      String name = result.get(0).value2();
      return new User(id, name, email);
   }

   public boolean createBudget(Budget budget, int ownerId) {
      final Result<BudgetsRecord> result =
              dbContext.insertInto(Budgets.BUDGETS,
                                   Budgets.BUDGETS.NAME,
                                   Budgets.BUDGETS.DESCRIPTION,
                                   Budgets.BUDGETS.OWNER_ID)
                       .values(budget.getName(), budget.getDescription(), ownerId)
                       .returning(Budgets.BUDGETS.ID)
                       .fetch();
      if (result.isEmpty())
         return false;
      int budgetId = result.get(0).value1();
      for (User user : budget.getParticipants()) {
         dbContext.insertInto(UserBudget.USER_BUDGET,
                              UserBudget.USER_BUDGET.BUDGET_ID,
                              UserBudget.USER_BUDGET.USER_ID)
                  .values(budgetId, user.getId())
                  .execute();
      }
      return true;
   }
}
