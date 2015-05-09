package tcs.javaproject.database;

import org.jooq.*;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Budgets;
import tcs.javaproject.database.tables.UserBudget;
import tcs.javaproject.database.tables.Users;
import tcs.javaproject.database.tables.records.BudgetsRecord;
import tcs.javaproject.guitest.Budget;
import tcs.javaproject.guitest.User;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

   public boolean validateUserPassword(String userEmail, String password) {
      Result<Record1<String>> result =
              dbContext.select(Users.USERS.PASSWORD_HASH)
                       .from(Users.USERS)
                       .where(Users.USERS.EMAIL.equal(userEmail))
                       .fetch();
      if (result.isEmpty())
         return false;
      final String expectedPassword = result.get(0).value1().trim();
      return password.equals(expectedPassword);
   }

   public User getUserByEmail(String email) {
      Result<Record2<Integer, String>> result =
              dbContext.select(Users.USERS.ID,
                               Users.USERS.NAME)
                       .from(Users.USERS)
                       .where(Users.USERS.EMAIL.equal(email))
                       .fetch();
      if (result.isEmpty())
         return null;
      final int id = result.get(0).value1();
      final String name = result.get(0).value2();
      return new User(id, name, email);
   }



   public boolean createUser(String email, String name, BigInteger bankAccount, String passwordHash) {
      dbContext.insertInto(Users.USERS,
                           Users.USERS.EMAIL,
                           Users.USERS.NAME,
                           Users.USERS.BANK_ACCOUNT,
                           Users.USERS.PASSWORD_HASH)
               .values(email, name, bankAccount, passwordHash)
               .execute();
      return true;
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

   public boolean deleteBudget(Budget budget) {
      dbContext.delete(UserBudget.USER_BUDGET)
               .where(UserBudget.USER_BUDGET.BUDGET_ID.equal(budget.getId()))
               .execute();
      dbContext.delete(Budgets.BUDGETS)
               .where(Budgets.BUDGETS.ID.equal(budget.getId()))
               .execute();
      return true;

   }

   public List<Budget> getAllBudgets(int ownerId) {
      Result<Record3<Integer, String, String>> result =
              dbContext.select(Budgets.BUDGETS.ID,
                               Budgets.BUDGETS.NAME,
                               Budgets.BUDGETS.DESCRIPTION)
                       .from(Budgets.BUDGETS)
                       .where(Budgets.BUDGETS.OWNER_ID.equal(ownerId))
                       .fetch();
      List<Budget> budgets = new ArrayList<>();
      for (Record3<Integer, String, String> budget : result) {
         final int id = budget.value1();
         final String name = budget.value2();
         final String description = budget.value3();
         final List<User> participants = getBudgetParticipants(id);
         budgets.add(new Budget(id, name, description, participants));
      }
      return budgets;
   }

   public List<User> getBudgetParticipants(int budgetId) {
      Result<Record3<Integer, String, String>> result =
              dbContext.select(Users.USERS.ID,
                               Users.USERS.NAME,
                               Users.USERS.EMAIL)
                       .from(UserBudget.USER_BUDGET
                                       .join(Users.USERS)
                                       .on(Users.USERS.ID.equal(UserBudget.USER_BUDGET.USER_ID)))
                       .where(UserBudget.USER_BUDGET.BUDGET_ID.equal(budgetId))
                       .fetch();
      List<User> participants = new ArrayList<>(result.size());
      for (Record3<Integer, String, String> user : result) {
         final int id = user.value1();
         final String name = user.value2();
         final String email = user.value3();
         participants.add(new User(id, name, email));
      }
      return participants;
   }
}
