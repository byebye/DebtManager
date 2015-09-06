package server;

import common.connection.DbHandler;
import common.data.BankTransfer;
import common.data.Budget;
import common.data.Email;
import common.data.Payment;
import common.data.Settlement;
import common.data.User;
import server.jooq.tables.BankTransfers;
import server.jooq.tables.Budgets;
import server.jooq.tables.Payments;
import server.jooq.tables.Settlements;
import server.jooq.tables.UserBudget;
import server.jooq.tables.Users;
import server.jooq.tables.records.BudgetsRecord;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseController implements DbHandler {

  private static DatabaseController onlyInstance;
  private static DbHandler exportedInstance;
  private static String dbUser = "debtmanager"; // "z1111813";
  private static String dbPassword = "debtmanager"; // "rU7i7xWoLVdh";
  private static String url = "jdbc:postgresql://localhost/debtmanager";//"jdbc:postgresql://db.tcs.uj.edu.pl/z1111813";
  private Connection connection;
  private DSLContext dbContext;

  // TODO lock that will be used to synchronize ALL operations on the database
  public static ReentrantReadWriteLock dbLock = new ReentrantReadWriteLock();

  private DatabaseController() {
  }

  public static void createInstance(String dbUser, String dbPassword, String url) throws InstantiationException {
    if (onlyInstance != null)
      throw new InstantiationException("instance already created");
    DatabaseController.dbUser = dbUser;
    DatabaseController.dbPassword = dbPassword;
    DatabaseController.url = url;
    onlyInstance = new DatabaseController();
    onlyInstance.connect();
  }

  public static void createExportedInstance() {
    try {
      exportedInstance = (DbHandler) UnicastRemoteObject.exportObject(DatabaseController.getInstance(), 1100);
    }
    catch (RemoteException re) {
      System.out.println("Exporting DbHandler failed");
    }
  }

  public static DatabaseController getInstance() {
    if (onlyInstance == null)
      throw new NullPointerException();
    return onlyInstance;
  }

  public static DbHandler getExportedInstance() {
    if (exportedInstance == null)
      throw new NullPointerException();
    return exportedInstance;
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

  public synchronized boolean validateUserPassword(Email email, String passwordHash) {
    Result<Record1<String>> result =
        dbContext.select(Users.USERS.PASSWORD_HASH)
            .from(Users.USERS)
            .where(Users.USERS.EMAIL.equal(email.toString()))
            .fetch();
    if (result.isEmpty())
      return false;
    final String expectedPassword = result.get(0).value1().trim();
    return passwordHash.equals(expectedPassword);
  }

  public synchronized User getUserByEmail(String email) {
    Result<Record3<Integer, String, String>> result =
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
    final String bankAccount = result.get(0).value3();
    return new User(id, name, email, bankAccount);
  }

  public synchronized User getUserById(int userId) {
    Result<Record3<String, String, String>> result =
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
    final String bankAccount = result.get(0).value3();
    return new User(userId, name, email, bankAccount);
  }

  public synchronized String getBudgetName(int id) {
    Result<Record1<String>> result =
        dbContext.select(Budgets.BUDGETS.NAME)
            .from(Budgets.BUDGETS)
            .where(Budgets.BUDGETS.ID.equal(id))
            .fetch();
    if (result.isEmpty())
      throw new NoSuchElementException("No such budget");
    final String name = result.get(0).value1();
    return name;
  }

  @Override
  public synchronized boolean createUser(User user, String passwordHash) {
    dbContext.insertInto(Users.USERS,
        Users.USERS.EMAIL,
        Users.USERS.NAME,
        Users.USERS.BANK_ACCOUNT,
        Users.USERS.PASSWORD_HASH)
        .values(user.getEmail().toString(), user.getName(), user.getBankAccount().toString(), passwordHash)
        .execute();
    return true;
  }

  public synchronized boolean createUser(String email,
      String name,
      String passwordHash,
      String bankAccount
  ) {
    dbContext.insertInto(Users.USERS,
        Users.USERS.EMAIL,
        Users.USERS.NAME,
        Users.USERS.BANK_ACCOUNT,
        Users.USERS.PASSWORD_HASH)
        .values(email, name, bankAccount, passwordHash)
        .execute();
    return true;
  }

  public synchronized boolean createBudget(Budget budget) {
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

    Server.slh.unhangAll();

    return true;
  }

  public synchronized boolean deleteBudget(Budget budget) {
    dbContext.delete(UserBudget.USER_BUDGET)
        .where(UserBudget.USER_BUDGET.BUDGET_ID.equal(budget.getId()))
        .execute();
    dbContext.delete(Payments.PAYMENTS)
        .where(Payments.PAYMENTS.BUDGET_ID.equal(budget.getId()))
        .execute();
    Result<Record1<Integer>> settlements =
        dbContext.select(Settlements.SETTLEMENTS.ID)
            .from(Settlements.SETTLEMENTS)
            .where(Settlements.SETTLEMENTS.BUDGET_ID.equal(budget.getId()))
            .fetch();

    for (Record1<Integer> settlement : settlements) {
      dbContext.delete(BankTransfers.BANK_TRANSFERS)
          .where(BankTransfers.BANK_TRANSFERS.SETTLEMENT_ID.equal(settlement.value1()))
          .execute();

      dbContext.delete(Settlements.SETTLEMENTS)
          .where(Settlements.SETTLEMENTS.ID.equal(settlement.value1()))
          .execute();
    }

    dbContext.delete(Budgets.BUDGETS)
        .where(Budgets.BUDGETS.ID.equal(budget.getId()))
        .execute();

    Server.slh.unhangAll();

    return true;
  }

  public synchronized List<Budget> getAllBudgets(int userId) {
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

  public synchronized List<User> getBudgetParticipants(int budgetId) {
    Result<Record4<Integer, String, String, String>> result =
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
    for (Record4<Integer, String, String, String> user : result) {
      final int id = user.value1();
      final String name = user.value2();
      final String email = user.value3();
      final String bankAccount = user.value4();
      participants.add(new User(id, name, email, bankAccount));
    }
    return participants;
  }

  public synchronized void addBudgetParticipants(int budgetId, List<User> users) {
    for (User user : users) {
      dbContext.insertInto(UserBudget.USER_BUDGET,
          UserBudget.USER_BUDGET.BUDGET_ID,
          UserBudget.USER_BUDGET.USER_ID)
          .values(budgetId, user.getId())
          .execute();
    }

    Server.slh.unhangAll();
  }

  public synchronized void addPayment(Budget budget, int userId, BigDecimal amount, String description) {
    dbContext.insertInto(Payments.PAYMENTS,
        Payments.PAYMENTS.BUDGET_ID,
        Payments.PAYMENTS.AMOUNT,
        Payments.PAYMENTS.PAYER_ID,
        Payments.PAYMENTS.DESCRIPTION)
        .values(budget.getId(),
            amount,
            userId,
            description)
        .execute();

    Server.slh.unhangAll();
  }

  public synchronized void updatePayment(int paymentId, int userId, BigDecimal amount, String description) {
    dbContext.update(Payments.PAYMENTS)
        .set(Payments.PAYMENTS.AMOUNT, amount)
        .set(Payments.PAYMENTS.PAYER_ID, userId)
        .set(Payments.PAYMENTS.DESCRIPTION, description)
        .where(Payments.PAYMENTS.ID.equal(paymentId))
        .execute();

    Server.slh.unhangAll();
  }

  public synchronized void deletePayment(int paymentId) {
    dbContext.delete(Payments.PAYMENTS)
        .where(Payments.PAYMENTS.ID.equal(paymentId))
        .execute();

    Server.slh.unhangAll();
  }

  public synchronized List<Settlement> getAllSettlements(int budgetId) {
    List<Settlement> settlements = new ArrayList<>();

    try {
      Result<Record2<Integer, java.sql.Date>> result =
          dbContext.select(Settlements.SETTLEMENTS.ID, Settlements.SETTLEMENTS.SETTLE_DATE)
              .from(Settlements.SETTLEMENTS)
              .where(Settlements.SETTLEMENTS.BUDGET_ID.equal(budgetId))
              .orderBy(Settlements.SETTLEMENTS.ID.desc())
              .fetch();

      for (Record2<Integer, Date> settlement : result) {
        int numPaidBankTransfers = dbContext.selectCount()
            .from(BankTransfers.BANK_TRANSFERS)
            .where(BankTransfers.BANK_TRANSFERS.PAID.equal(2))
            .and(BankTransfers.BANK_TRANSFERS.SETTLEMENT_ID.equal(settlement.value1()))
            .fetchOne().value1();
        int numAllBankTransfers = dbContext.selectCount()
            .from(BankTransfers.BANK_TRANSFERS)
            .where(BankTransfers.BANK_TRANSFERS.SETTLEMENT_ID.equal(settlement.value1()))
            .fetchOne().value1();

        double amount = 0.0;
        for (Payment p : getPaymentsBySettlementId(settlement.value1())) {
          amount += p.getAmount();
        }

        settlements.add(new Settlement(settlement.value1(), budgetId, numPaidBankTransfers, numAllBankTransfers,
            settlement.value2(), amount));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    return settlements;
  }

  public synchronized List<Payment> getPaymentsBySettlementId(int settlementId) {
    List<Payment> payments = new ArrayList<>();
    Result<Record1<Integer>> result =
        dbContext.select(Payments.PAYMENTS.ID)
            .from(Payments.PAYMENTS)
            .where(Payments.PAYMENTS.SETTLEMENT_ID.equal(settlementId))
            .fetch();

    for (Record1<Integer> id : result) {
      final int paymentId = id.value1();
      Record4<Integer, Integer, String, BigDecimal> payment =
          dbContext.select(Payments.PAYMENTS.BUDGET_ID,
              Payments.PAYMENTS.PAYER_ID,
              Payments.PAYMENTS.DESCRIPTION,
              Payments.PAYMENTS.AMOUNT)
              .from(Payments.PAYMENTS)
              .where(Payments.PAYMENTS.ID.equal(paymentId))
              .fetchOne();
      final int budgetId = payment.value1();
      final int userId = payment.value2();
      final String userName = getUserById(userId).getName();
      final String description = payment.value3();
      final double amount = payment.value4().doubleValue();
      payments.add(new Payment(paymentId, budgetId, userId, userName, description, amount));
    }
    return payments;
  }

  public synchronized List<Payment> getAllPayments(int budgetId, boolean accounted) {
    Result<Record4<Integer, Integer, String, BigDecimal>> result =
        dbContext.select(Payments.PAYMENTS.ID,
            Payments.PAYMENTS.PAYER_ID,
            Payments.PAYMENTS.DESCRIPTION,
            Payments.PAYMENTS.AMOUNT)
            .from(Payments.PAYMENTS)
            .where(Payments.PAYMENTS.BUDGET_ID.equal(budgetId))
            .and(Payments.PAYMENTS.SETTLED.equal(accounted)).fetch();

    List<Payment> payments = new ArrayList<>(result.size());
    for (Record4<Integer, Integer, String, BigDecimal> payment : result) {
      final int userId = payment.value2();
      final String userName = getUserById(userId).getName();
      final int paymentId = payment.value1();
      final String description = payment.value3();
      final double amount = payment.value4().doubleValue();
      payments.add(new Payment(paymentId, budgetId, userId, userName, description, amount));
    }
    return payments;
  }

  public synchronized List<BankTransfer> calculateBankTransfers(int budgetId, List<Payment> unsettledPayments) {
    List<Integer> usersBellowAverage = new ArrayList<>(), usersAboveAverage = new ArrayList<>();
    Map<Integer, Double> userSpend = new HashMap<>();
    double sum = 0;
    double userNum = (double) getBudgetParticipants(budgetId).size();
    for (User u : getBudgetParticipants(budgetId)) {
      userSpend.put(u.getId(), 0.0);
    }
    for (Payment p : unsettledPayments) {
      sum += p.getAmount();
      userSpend.put(p.getUserId(), userSpend.get(p.getUserId()) + p.getAmount());
    }

    for (Integer userId : userSpend.keySet()) {
      if (userSpend.get(userId) < sum / userNum)
        usersBellowAverage.add(userId);
      else if (userSpend.get(userId) > sum / userNum)
        usersAboveAverage.add(userId);
    }

    List<BankTransfer> neededTransfers = new ArrayList<>();

    while (!usersBellowAverage.isEmpty() && !usersAboveAverage.isEmpty()) {
      int userBellow = usersBellowAverage.remove(0), userAbove = usersAboveAverage.remove(0);
      neededTransfers.add(new BankTransfer(getUserById(userBellow), getUserById(userAbove),
          BigDecimal.valueOf(sum / userNum - userSpend.get(userBellow))));//drop paymentId field
      userSpend.put(userAbove, userSpend.get(userAbove) - (sum / userNum - userSpend.get(userBellow)));
      if (userSpend.get(userAbove) < sum / userNum)
        usersBellowAverage.add(userAbove);
      else if (userSpend.get(userAbove) > sum / userNum)
        usersAboveAverage.add(userAbove);
    }

    return neededTransfers;
  }

  public synchronized void settlePayments(int budgetId, List<Payment> payments,
      List<BankTransfer> bankTransfers, boolean sendEmails
  ) {
    int settleId = dbContext.insertInto(Settlements.SETTLEMENTS, Settlements.SETTLEMENTS.BUDGET_ID)
        .values(budgetId)
        .returning(Settlements.SETTLEMENTS.ID)
        .fetchOne().getId();
    for (Payment payment : payments) {
      dbContext.update(Payments.PAYMENTS)
          .set(Payments.PAYMENTS.SETTLED, true)
          .set(Payments.PAYMENTS.SETTLEMENT_ID, settleId)
          .where(Payments.PAYMENTS.ID.equal(payment.getId()))
          .execute();
    }

    for (BankTransfer bk : bankTransfers) {
      dbContext.insertInto(
          BankTransfers.BANK_TRANSFERS,
          BankTransfers.BANK_TRANSFERS.SETTLEMENT_ID,
          BankTransfers.BANK_TRANSFERS.SENDER,
          BankTransfers.BANK_TRANSFERS.RECIPIENT,
          BankTransfers.BANK_TRANSFERS.AMOUNT
      ).values(settleId, bk.getSenderId(), bk.getRecipientId(), bk.getAmount())
          .execute();
    }
    if (sendEmails)
      new Thread(new BankTransferEmailSender(budgetId, bankTransfers)).run();

    Server.slh.unhangAll();
  }

  public synchronized void removeParticipant(int budgetId, int userId) {
    dbContext.delete(UserBudget.USER_BUDGET)
        .where(UserBudget.USER_BUDGET.USER_ID.equal(userId)
            .and(UserBudget.USER_BUDGET.BUDGET_ID.equal(budgetId)))
        .execute();

    Server.slh.unhangAll();
  }

  public synchronized List<BankTransfer> getToSendBankTransfers(int userId) {
    List<BankTransfer> myBankTransfers = new ArrayList<>();
    Result<Record5<Integer, Integer, Integer, BigDecimal, Integer>> result =
        dbContext.select(
            BankTransfers.BANK_TRANSFERS.ID,
            BankTransfers.BANK_TRANSFERS.SETTLEMENT_ID,
            BankTransfers.BANK_TRANSFERS.RECIPIENT,
            BankTransfers.BANK_TRANSFERS.AMOUNT,
            BankTransfers.BANK_TRANSFERS.PAID
        ).from(BankTransfers.BANK_TRANSFERS)
            .where(BankTransfers.BANK_TRANSFERS.SENDER.equal(userId))
            .fetch();

    for (Record5<Integer, Integer, Integer, BigDecimal, Integer> bankTransfer : result) {
      int transferId = bankTransfer.value1();
      int settleId = bankTransfer.value2();
      int recipientId = bankTransfer.value3();
      BigDecimal amount = bankTransfer.value4();
      int status = bankTransfer.value5();

      User sender = getUserById(userId);
      User recipient = getUserById(recipientId);
      int budgetId =
          dbContext.select(Settlements.SETTLEMENTS.BUDGET_ID)
              .from(Settlements.SETTLEMENTS)
              .where(Settlements.SETTLEMENTS.ID.equal(settleId))
              .fetchOne().value1();
      String budgetName =
          dbContext.select(Budgets.BUDGETS.NAME)
              .from(Budgets.BUDGETS)
              .where(Budgets.BUDGETS.ID.equal(budgetId))
              .fetchOne().value1();

      myBankTransfers.add(new BankTransfer(transferId, budgetName, sender, recipient, amount, status));
    }
    return myBankTransfers;
  }

  public synchronized List<BankTransfer> getToReceiveBankTransfers(int userId) {
    List<BankTransfer> toReceiveBankTransfers = new ArrayList<>();
    Result<Record5<Integer, Integer, Integer, BigDecimal, Integer>> result =
        dbContext.select(
            BankTransfers.BANK_TRANSFERS.ID,
            BankTransfers.BANK_TRANSFERS.SETTLEMENT_ID,
            BankTransfers.BANK_TRANSFERS.SENDER,
            BankTransfers.BANK_TRANSFERS.AMOUNT,
            BankTransfers.BANK_TRANSFERS.PAID
        ).from(BankTransfers.BANK_TRANSFERS)
            .where(BankTransfers.BANK_TRANSFERS.RECIPIENT.equal(userId))
            .fetch();

    for (Record5<Integer, Integer, Integer, BigDecimal, Integer> bankTransfer : result) {
      int transferId = bankTransfer.value1();
      int settleId = bankTransfer.value2();
      int senderId = bankTransfer.value3();
      BigDecimal amount = bankTransfer.value4();
      int status = bankTransfer.value5();

      int budgetId =
          dbContext.select(Settlements.SETTLEMENTS.BUDGET_ID)
              .from(Settlements.SETTLEMENTS)
              .where(Settlements.SETTLEMENTS.ID.equal(settleId))
              .fetchOne().value1();
      String budgetName =
          dbContext.select(Budgets.BUDGETS.NAME)
              .from(Budgets.BUDGETS)
              .where(Budgets.BUDGETS.ID.equal(budgetId))
              .fetchOne().value1();

      final User sender = getUserById(senderId);
      final User recipient = getUserById(userId);

      toReceiveBankTransfers.add(new BankTransfer(transferId, budgetName, sender, recipient, amount, status));
    }
    return toReceiveBankTransfers;
  }

  public synchronized List<BankTransfer> getBankTransfersBySettlementId(int settlementId) {
    List<BankTransfer> bankTransfers = new ArrayList<>();

    int budgetId =
        dbContext.select(Settlements.SETTLEMENTS.BUDGET_ID)
            .from(Settlements.SETTLEMENTS)
            .where(Settlements.SETTLEMENTS.ID.equal(settlementId))
            .fetchOne().value1();
    String budgetName =
        dbContext.select(Budgets.BUDGETS.NAME)
            .from(Budgets.BUDGETS)
            .where(Budgets.BUDGETS.ID.equal(budgetId))
            .fetchOne().value1();

    Result<Record5<Integer, Integer, Integer, BigDecimal, Integer>> result =
        dbContext.select(
            BankTransfers.BANK_TRANSFERS.ID,
            BankTransfers.BANK_TRANSFERS.SENDER,
            BankTransfers.BANK_TRANSFERS.RECIPIENT,
            BankTransfers.BANK_TRANSFERS.AMOUNT,
            BankTransfers.BANK_TRANSFERS.PAID)
            .from(BankTransfers.BANK_TRANSFERS)
            .where(BankTransfers.BANK_TRANSFERS.SETTLEMENT_ID.equal(settlementId))
            .fetch();

    for (Record5<Integer, Integer, Integer, BigDecimal, Integer> bankTransfer : result) {
      bankTransfers.add(
          new BankTransfer(
              bankTransfer.value1(),
              budgetName,
              getUserById(bankTransfer.value2()),
              getUserById(bankTransfer.value3()),
              bankTransfer.value4(),
              bankTransfer.value5()
          )
      );
    }
    return bankTransfers;
  }

  @Override
  public synchronized void setBankTransfersStatus(int transferId, int status) throws RemoteException {
    dbContext.update(BankTransfers.BANK_TRANSFERS)
        .set(BankTransfers.BANK_TRANSFERS.PAID, status)
        .where(BankTransfers.BANK_TRANSFERS.ID.equal(transferId))
        .execute();
  }

  @Override
  public synchronized void setBankTransfersStatus(Map<Integer, Integer> bankTransfers) throws RemoteException {
    for (Map.Entry<Integer, Integer> transfer : bankTransfers.entrySet()) {
      setBankTransfersStatus(transfer.getKey(), transfer.getValue());
    }

    Server.slh.unhangAll();
  }
}
