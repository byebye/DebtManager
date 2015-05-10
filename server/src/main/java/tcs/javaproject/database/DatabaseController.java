package tcs.javaproject.database;

import org.jooq.*;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Budgets;
import tcs.javaproject.database.tables.Payments;
import tcs.javaproject.database.tables.UserBudget;
import tcs.javaproject.database.tables.Users;
import tcs.javaproject.database.tables.records.BudgetsRecord;
import tcs.javaproject.guitest.Budget;
import tcs.javaproject.guitest.Payment;
import tcs.javaproject.guitest.User;

import java.math.BigDecimal;
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

   public User getUserById(int userId) {
      Result<Record2<String, String>> result =
              dbContext.select(Users.USERS.EMAIL,
                               Users.USERS.NAME)
                       .from(Users.USERS)
                       .where(Users.USERS.ID.equal(userId))
                       .fetch();
      if (result.isEmpty())
         return null;
      final String email = result.get(0).value1();
      final String name = result.get(0).value2();
      return new User(userId, name, email);
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

   public boolean createBudget(Budget budget) {
      final BudgetsRecord result =
              dbContext.insertInto(Budgets.BUDGETS,
                                   Budgets.BUDGETS.NAME,
                                   Budgets.BUDGETS.DESCRIPTION,
                                   Budgets.BUDGETS.OWNER_ID)
                       .values(budget.getName(), budget.getDescription(), budget.getOwner().getId())
                       .returning(Budgets.BUDGETS.ID)
                       .fetchOne();
      final int budgetId = result.getId();
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

   public List<Budget> getAllBudgets(int userId) {
      Result<Record4<Integer, Integer, String, String>> result =
              dbContext.select(Budgets.BUDGETS.ID,
                               Budgets.BUDGETS.OWNER_ID,
                               Budgets.BUDGETS.NAME,
                               Budgets.BUDGETS.DESCRIPTION)
                       .from(Budgets.BUDGETS)
                       .where(Budgets.BUDGETS.OWNER_ID
                                     .equal(userId)
                                     .or(Budgets.BUDGETS.ID
                                                 .in(dbContext.select(UserBudget.USER_BUDGET.BUDGET_ID)
                                                              .from(UserBudget.USER_BUDGET)
                                                              .where(UserBudget.USER_BUDGET.USER_ID
                                                                             .equal(userId)))
                                     )
                       )
                       .fetch();
      List<Budget> budgets = new ArrayList<>();
      for (Record4<Integer, Integer, String, String> budget : result) {
         final int id = budget.value1();
         final int ownerId = budget.value2();
         final User owner = getUserById(ownerId);
         final String name = budget.value3();
         final String description = budget.value4();
         final List<User> participants = getBudgetParticipants(id);
         budgets.add(new Budget(id, owner, name, description, participants));
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

   public void addBudgetParticipants(int budgetId, List<User> users) {
      for (User user : users) {
         dbContext.insertInto(UserBudget.USER_BUDGET,
                              UserBudget.USER_BUDGET.BUDGET_ID,
                              UserBudget.USER_BUDGET.USER_ID)
                  .values(budgetId, user.getId())
                  .execute();
      }
   }

   public void addPayment(Budget budget, int userId, BigDecimal amount, String what) {
      dbContext.insertInto(Payments.PAYMENTS,
                           Payments.PAYMENTS.BUDGET_ID,
                           Payments.PAYMENTS.AMOUNT,
                           Payments.PAYMENTS.USER_ID,
                           Payments.PAYMENTS.DESCRIPTION)
               .values(budget.getId(),
                       amount,
                       userId,
                       what)
               .execute();
   }

   public List<Payment> getAllPayments(int budgetId, boolean accounted) {
      Result<Record4<Integer, Integer, String, BigDecimal>> result =
              dbContext.select(Payments.PAYMENTS.ID,
                               Payments.PAYMENTS.USER_ID,
                               Payments.PAYMENTS.DESCRIPTION,
                               Payments.PAYMENTS.AMOUNT)
                       .from(Payments.PAYMENTS)
                       .where(Payments.PAYMENTS.BUDGET_ID.equal(budgetId))
                       .and(Payments.PAYMENTS.ACCOUNTED.equal(accounted)).fetch();

      List<Payment> payments = new ArrayList<>(result.size());
      for (Record4<Integer, Integer, String, BigDecimal> payment : result) {
         final int userId = payment.value2();
         final String userName = getUserById(userId).getName();
         final int paymentId = payment.value1();
         final String description = payment.value3();
         final double amount = payment.value4().doubleValue();
         payments.add(new Payment(userName, description, amount, paymentId));
      }
      return payments;
   }

   public void settleUnaccountedPayments(int budgetId) {
      dbContext.update(Payments.PAYMENTS)
               .set(Payments.PAYMENTS.ACCOUNTED, true)
               .where(Payments.PAYMENTS.BUDGET_ID.equal(budgetId))
               .execute();
   }
}
