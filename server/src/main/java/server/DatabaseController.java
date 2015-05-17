package server;

import common.*;
import server.jooq.tables.Budgets;
import server.jooq.tables.Payments;
import server.jooq.tables.Users;
import server.jooq.tables.records.BudgetsRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import server.jooq.tables.UserBudget;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseController implements DBHandler {
   private final String dbUser = "debtmanager";
   private final String dbPassword = "debtmanager";
   private final String url = "jdbc:postgresql://localhost/debtmanager";
   private Connection connection;
   private DSLContext dbContext;

   //lock that will be used to synchronize ALL operations on the database
   public static ReentrantReadWriteLock dbLock = new ReentrantReadWriteLock();

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

   @Override
   public boolean validateUserPassword(Email email, String passwordHash) {
      Result<Record1<String>> result =
              dbContext.select(Users.USERS.PASSWORD_HASH)
                       .from(Users.USERS)
                       .where(Users.USERS.EMAIL.equal(email.getAddress()))
                       .fetch();
      if (result.isEmpty())
         return false;
      final String expectedPassword = result.get(0).value1().trim();
      return passwordHash.equals(expectedPassword);
   }

   public User getUserByEmail(String email) {
      Result<Record3<Integer, String, BigInteger>> result =
              dbContext.select(Users.USERS.ID,
                               Users.USERS.NAME,
                               Users.USERS.BANK_ACCOUNT)
                       .from(Users.USERS)
                       .where(Users.USERS.EMAIL.equal(email))
                       .fetch();
      if (result.isEmpty())
         return null;
      final int id = result.get(0).value1();
      final String name = result.get(0).value2();
      final String bankAccount = result.get(0).value3().toString();
      return new User(id, name, email, bankAccount);
   }

   public User getUserById(int userId) {
      Result<Record3<String, String, BigInteger>> result =
              dbContext.select(Users.USERS.EMAIL,
                               Users.USERS.NAME,
                               Users.USERS.BANK_ACCOUNT)
                       .from(Users.USERS)
                       .where(Users.USERS.ID.equal(userId))
                       .fetch();
      if (result.isEmpty())
         return null;
      final String email = result.get(0).value1();
      final String name = result.get(0).value2();
      final String bankAccount = result.get(0).value3().toString();
      return new User(userId, name, email, bankAccount);
   }

   @Override
   public boolean createUser(User user, String passwordHash) {
      BigInteger bankAccount = new BigInteger(user.getBankAccount());
      dbContext.insertInto(Users.USERS,
                           Users.USERS.EMAIL,
                           Users.USERS.NAME,
                           Users.USERS.BANK_ACCOUNT,
                           Users.USERS.PASSWORD_HASH)
               .values(user.getEmail(), user.getName(), bankAccount, passwordHash)
               .execute();
      return true;
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
      dbContext.delete(Payments.PAYMENTS)
               .where(Payments.PAYMENTS.BUDGET_ID.equal(budget.getId()))
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
                       .where(Budgets.BUDGETS.OWNER_ID.equal(userId)
                       .or(Budgets.BUDGETS.ID
                                   .in(dbContext.select(UserBudget.USER_BUDGET.BUDGET_ID)
                                                .from(UserBudget.USER_BUDGET)
                                                .where(UserBudget.USER_BUDGET.USER_ID.equal(userId)))
                       ))
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
      Result<Record4<Integer, String, String, BigInteger>> result =
              dbContext.select(Users.USERS.ID,
                               Users.USERS.NAME,
                               Users.USERS.EMAIL,
                               Users.USERS.BANK_ACCOUNT)
                       .from(UserBudget.USER_BUDGET
                       .join(Users.USERS)
                       .on(Users.USERS.ID.equal(UserBudget.USER_BUDGET.USER_ID)))
                       .where(UserBudget.USER_BUDGET.BUDGET_ID.equal(budgetId))
                       .fetch();
      List<User> participants = new ArrayList<>(result.size());
      for (Record4<Integer, String, String, BigInteger> user : result) {
         final int id = user.value1();
         final String name = user.value2();
         final String email = user.value3();
         final String bankAccount = user.value4().toString();
         participants.add(new User(id, name, email, bankAccount));
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

   public void updatePayment(int paymentId, int userId, BigDecimal amount, String what) {
      dbContext.update(Payments.PAYMENTS)
               .set(Payments.PAYMENTS.AMOUNT, amount)
               .set(Payments.PAYMENTS.USER_ID, userId)
               .set(Payments.PAYMENTS.DESCRIPTION, what)
               .where(Payments.PAYMENTS.ID.equal(paymentId))
               .execute();
   }

   public void deletePayment(int paymentId) {
      dbContext.delete(Payments.PAYMENTS)
               .where(Payments.PAYMENTS.ID.equal(paymentId))
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
         payments.add(new Payment(budgetId, userId, userName, description, amount, paymentId));
      }
      return payments;
   }

   public List<BankTransfer> calculateBankTransfers(int budgetId) {
      List<Payment> unaccountedPayments = getAllPayments(budgetId, false);
      List<BankTransfer> neededTransfers = new ArrayList<>();
      for(Payment payment : unaccountedPayments) {
         final int whoId = payment.getUserId();
         final int whomId = whoId;
         neededTransfers.add(new BankTransfer(getUserById(whoId), getUserById(whomId), new BigDecimal(0)));
      }
      return neededTransfers;
   }

   public void settleUnaccountedPayments(int budgetId) {
      dbContext.update(Payments.PAYMENTS)
               .set(Payments.PAYMENTS.ACCOUNTED, true)
               .where(Payments.PAYMENTS.BUDGET_ID.equal(budgetId))
               .execute();
   }

   public void removeParticipant(int budgetId, int userId) {
      dbContext.delete(UserBudget.USER_BUDGET)
               .where(UserBudget.USER_BUDGET.USER_ID.equal(userId)
               .and(UserBudget.USER_BUDGET.BUDGET_ID.equal(budgetId)))
               .execute();
   }

}