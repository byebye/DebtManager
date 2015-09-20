package server;

import common.connection.DbHandler;
import common.data.BankTransfer;
import common.data.BankTransfer.Status;
import common.data.Budget;
import common.data.Email;
import common.data.Payment;
import common.data.Settlement;
import common.data.User;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static server.jooq.tables.BankTransfers.BANK_TRANSFERS;
import static server.jooq.tables.Budgets.BUDGETS;
import static server.jooq.tables.Payments.PAYMENTS;
import static server.jooq.tables.PaymentsOwingUsers.PAYMENTS_OWING_USERS;
import static server.jooq.tables.Settlements.SETTLEMENTS;
import static server.jooq.tables.UserBudget.USER_BUDGET;
import static server.jooq.tables.Users.USERS;

public class DatabaseController implements DbHandler {

  private static DatabaseController onlyInstance;
  private static DbHandler exportedInstance;
  private static String dbUser = "debtmanager";
  private static String dbPassword = "debtmanager";
  private static String url = "jdbc:postgresql://localhost/debtmanager";
  private Connection connection;
  private DSLContext dbContext;

  private DatabaseController() {
  }

  public synchronized static void createInstance(String dbUser, String dbPassword, String url
  ) throws InstantiationException {
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

  private synchronized void connect() {
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
    final Record1<String> result = dbContext
        .select(USERS.PASSWORD_HASH)
        .from(USERS)
        .where(USERS.EMAIL.equal(email.toString()))
        .fetchOne();
    if (result == null)
      return false;
    final String expectedPasswordHash = result.value1().trim();
    return Objects.equals(passwordHash, expectedPasswordHash);
  }

  public synchronized String getBudgetName(int budgetId) {
    return dbContext
        .select(BUDGETS.NAME)
        .from(BUDGETS)
        .where(BUDGETS.ID.equal(budgetId))
        .fetchOne()
        .value1();
  }

  @Override
  public synchronized User getUserByEmail(String email) {
    return getUser(USERS.EMAIL.equal(email));
  }

  @Override
  public synchronized User getUserById(int userId) {
    return getUser(USERS.ID.equal(userId));
  }

  private synchronized User getUser(Condition condition) {
    Record4<Integer, String, String, String> result = dbContext
        .select(
            USERS.ID,
            USERS.EMAIL,
            USERS.NAME,
            USERS.BANK_ACCOUNT)
        .from(USERS)
        .where(condition)
        .fetchOne();
    if (result == null)
      return null;
    return extractIntoUser(result);
  }

  private User extractIntoUser(Record4<Integer, String, String, String> result) {
    final int userId = result.value1();
    final String email = result.value2();
    final String name = result.value3();
    final String bankAccount = result.value4();
    return new User(userId, name, email, bankAccount);
  }

  @Override
  public synchronized boolean createUser(String email, String name, String bankAccount, String passwordHash) {
    final int createdUsersCount = dbContext
        .insertInto(USERS,
            USERS.EMAIL,
            USERS.NAME,
            USERS.BANK_ACCOUNT,
            USERS.PASSWORD_HASH)
        .values(email, name, bankAccount, passwordHash)
        .execute();
    return createdUsersCount == 1;
  }

  @Override
  public synchronized boolean createBudget(Budget budget) {
    dbContext.transaction(configuration -> {
      final DSLContext transactionContext = DSL.using(configuration);
      final int budgetId = transactionContext
          .insertInto(BUDGETS,
              BUDGETS.NAME,
              BUDGETS.DESCRIPTION,
              BUDGETS.OWNER_ID)
          .values(budget.getName(), budget.getDescription(), budget.getOwner().getId())
          .returning(BUDGETS.ID)
          .fetchOne()
          .getValue(BUDGETS.ID);
      for (User user : budget.getParticipants()) {
        transactionContext
            .insertInto(USER_BUDGET,
                USER_BUDGET.BUDGET_ID,
                USER_BUDGET.USER_ID)
            .values(budgetId, user.getId())
            .execute();
      }
    });

    Server.slh.unhangAll();
    return true;
  }

  @Override
  public synchronized boolean deleteBudget(Budget budget) {
    final int deletedBudgetsCount = dbContext
        .delete(BUDGETS)
        .where(BUDGETS.ID.equal(budget.getId()))
        .execute();

    Server.slh.unhangAll();
    return deletedBudgetsCount == 1;
  }

  @Override
  public synchronized List<Budget> getAllBudgets(int userId) {
    return dbContext
        .select(
            BUDGETS.ID,
            BUDGETS.OWNER_ID,
            BUDGETS.NAME,
            BUDGETS.DESCRIPTION)
        .from(USER_BUDGET)
        .join(BUDGETS)
        .on(BUDGETS.ID.equal(USER_BUDGET.BUDGET_ID))
        .where(USER_BUDGET.USER_ID.equal(userId))
        .fetch()
        .stream()
        .map(this::extractIntoBudget)
        .collect(Collectors.toList());
  }

  private Budget extractIntoBudget(Record4<Integer, Integer, String, String> budget) {
    final int id = budget.value1();
    final int ownerId = budget.value2();
    final User owner = getUserById(ownerId);
    final String name = budget.value3();
    final String description = budget.value4();
    final List<User> participants = getBudgetParticipants(id);
    return new Budget(id, owner, name, description, participants);
  }

  @Override
  public synchronized List<User> getBudgetParticipants(int budgetId) {
    return dbContext
        .select(
            USERS.ID,
            USERS.EMAIL,
            USERS.NAME,
            USERS.BANK_ACCOUNT)
        .from(USER_BUDGET)
        .join(USERS)
        .on(USERS.ID.equal(USER_BUDGET.USER_ID))
        .where(USER_BUDGET.BUDGET_ID.equal(budgetId))
        .fetch()
        .stream()
        .map(this::extractIntoUser)
        .collect(Collectors.toList());
  }

  @Override
  public synchronized void addBudgetParticipants(int budgetId, List<User> users) {
    dbContext.transaction(configuration -> {
      for (User user : users) {
        DSL.using(configuration)
            .insertInto(USER_BUDGET,
                USER_BUDGET.BUDGET_ID,
                USER_BUDGET.USER_ID)
            .values(budgetId, user.getId())
            .execute();
      }
    });
    Server.slh.unhangAll();
  }

  @Override
  public synchronized void addPayment(Budget budget, int userId, BigDecimal amount, String description,
      Collection<Integer> owingUserIds) {
    dbContext.transaction(configuration -> {
      DSLContext transactionContext = DSL.using(configuration);
      final int paymentId = insertPayment(transactionContext, budget, userId, amount, description);
      insertOwingUsers(transactionContext, paymentId, new HashSet<>(owingUserIds));
    });
    Server.slh.unhangAll();
  }

  private int insertPayment(DSLContext transactionContext, Budget budget, int userId, BigDecimal amount,
      String description) {
    return transactionContext
        .insertInto(PAYMENTS,
            PAYMENTS.BUDGET_ID,
            PAYMENTS.AMOUNT,
            PAYMENTS.PAYER_ID,
            PAYMENTS.DESCRIPTION)
        .values(budget.getId(),
            amount,
            userId,
            description)
        .returning(PAYMENTS.ID)
        .fetchOne()
        .getValue(PAYMENTS.ID);
  }

  private void insertOwingUsers(DSLContext transactionContext, int paymentId, Set<Integer> owingUserIds) {
    owingUserIds.forEach(owingUserId ->
        transactionContext
            .insertInto(PAYMENTS_OWING_USERS,
                PAYMENTS_OWING_USERS.PAYMENT_ID,
                PAYMENTS_OWING_USERS.USER_ID)
            .values(paymentId, owingUserId)
            .execute());
  }

  @Override
  public synchronized void updatePayment(int paymentId, int userId, BigDecimal amount, String description,
      Collection<Integer> owingUserIds) {
    dbContext.transaction(configuration -> {
      DSLContext transactionContext = DSL.using(configuration);
      updatePayment(transactionContext, paymentId, userId, amount, description);
      updateOwingUsers(transactionContext, paymentId, new HashSet<>(owingUserIds));
    });
    Server.slh.unhangAll();
  }

  private void updatePayment(DSLContext transactionContext, int paymentId, int userId, BigDecimal amount,
      String description) {
    transactionContext
        .update(PAYMENTS)
        .set(PAYMENTS.AMOUNT, amount)
        .set(PAYMENTS.PAYER_ID, userId)
        .set(PAYMENTS.DESCRIPTION, description)
        .where(PAYMENTS.ID.equal(paymentId))
        .execute();
  }

  private void updateOwingUsers(DSLContext transactionContext, int paymentId, Set<Integer> newOwingUserIds) {
    final Set<Integer> currentOwingUsers = getCurrentOwingUsers(transactionContext, paymentId);
    final Set<Integer> usersToRetain = new HashSet<>(newOwingUserIds);
    usersToRetain.retainAll(currentOwingUsers);
    currentOwingUsers.removeAll(usersToRetain);
    deleteOwingUsers(transactionContext, paymentId, currentOwingUsers);
    newOwingUserIds.removeAll(usersToRetain);
    newOwingUserIds.forEach(owingUserId ->
        transactionContext
            .insertInto(PAYMENTS_OWING_USERS,
                PAYMENTS_OWING_USERS.PAYMENT_ID,
                PAYMENTS_OWING_USERS.USER_ID)
            .values(paymentId, owingUserId)
            .execute());
  }

  private Set<Integer> getCurrentOwingUsers(DSLContext transactionContext, int paymentId) {
    return transactionContext
        .select(PAYMENTS_OWING_USERS.USER_ID)
        .from(PAYMENTS_OWING_USERS)
        .where(PAYMENTS_OWING_USERS.PAYMENT_ID.equal(paymentId))
        .fetchSet(PAYMENTS_OWING_USERS.USER_ID);
  }

  private void deleteOwingUsers(DSLContext transactionContext, int paymentId, Set<Integer> owingUserIds) {
    transactionContext
        .deleteFrom(PAYMENTS_OWING_USERS)
        .where(PAYMENTS_OWING_USERS.PAYMENT_ID.equal(paymentId))
        .and(PAYMENTS_OWING_USERS.USER_ID.in(owingUserIds))
        .execute();
  }

  @Override
  public synchronized void deletePayment(int paymentId) {
    dbContext
        .delete(PAYMENTS)
        .where(PAYMENTS.ID.equal(paymentId))
        .execute();

    Server.slh.unhangAll();
  }

  @Override
  public synchronized List<Settlement> getAllSettlementsOfBudget(int budgetId) {
    return dbContext
        .select(
            SETTLEMENTS.ID,
            SETTLEMENTS.SETTLE_DATE)
        .from(SETTLEMENTS)
        .where(SETTLEMENTS.BUDGET_ID.equal(budgetId))
        .orderBy(SETTLEMENTS.ID.desc())
        .fetch()
        .stream()
        .map(settlement -> extractIntoSettlement(budgetId, settlement))
        .collect(Collectors.toList());
  }

  private Settlement extractIntoSettlement(int budgetId, Record2<Integer, Date> settlement) {
    final int settlementId = settlement.value1();
    final int paidTransfersCount = getPaidBankTransfersCount(settlementId);
    final int allTransfersCount = getAllBankTransfersCount(settlementId);
    final BigDecimal spentMoney = calculateSettlementSpentMoney(settlementId);
    final Date settleDate = settlement.value2();
    return new Settlement(settlementId, budgetId, paidTransfersCount, allTransfersCount, settleDate, spentMoney);
  }

  private synchronized BigDecimal calculateSettlementSpentMoney(int settlementId) {
    return dbContext
        .select(DSL.coalesce(PAYMENTS.AMOUNT.sum().cast(BigDecimal.class), DSL.value(0.0)))
        .from(PAYMENTS)
        .where(PAYMENTS.SETTLEMENT_ID.equal(settlementId))
        .fetchOne()
        .value1();
  }

  private synchronized int getPaidBankTransfersCount(int settlementId) {
    return dbContext
        .selectCount()
        .from(BANK_TRANSFERS)
        .where(BANK_TRANSFERS.STATUS.equal(Status.Confirmed.getValue()))
        .and(BANK_TRANSFERS.SETTLEMENT_ID.equal(settlementId))
        .fetchOne().value1();
  }

  private synchronized int getAllBankTransfersCount(int settlementId) {
    return dbContext
        .selectCount()
        .from(BANK_TRANSFERS)
        .where(BANK_TRANSFERS.SETTLEMENT_ID.equal(settlementId))
        .fetchOne().value1();
  }

  @Override
  public synchronized List<Payment> getPaymentsBySettlementId(int settlementId) {
    return getPayments(PAYMENTS.SETTLEMENT_ID.equal(settlementId));
  }

  @Override
  public synchronized List<Payment> getAllPayments(int budgetId, boolean settled) {
    return getPayments(PAYMENTS.BUDGET_ID.equal(budgetId).and(PAYMENTS.SETTLED.equal(settled)));
  }

  private synchronized List<Payment> getPayments(Condition condition) {
    return dbContext
        .select(
            PAYMENTS.ID,
            PAYMENTS.BUDGET_ID,
            PAYMENTS.PAYER_ID,
            PAYMENTS.DESCRIPTION,
            PAYMENTS.AMOUNT,
            DSL.arrayAgg(PAYMENTS_OWING_USERS.USER_ID))
        .from(PAYMENTS)
        .join(PAYMENTS_OWING_USERS)
        .on(PAYMENTS_OWING_USERS.PAYMENT_ID.equal(PAYMENTS.ID))
        .where(condition)
        .groupBy(
            PAYMENTS.ID,
            PAYMENTS.BUDGET_ID,
            PAYMENTS.PAYER_ID,
            PAYMENTS.DESCRIPTION,
            PAYMENTS.AMOUNT)
        .fetch()
        .stream()
        .map(this::extractIntoPayment)
        .collect(Collectors.toList());
  }

  private Payment extractIntoPayment(Record6<Integer, Integer, Integer, String, BigDecimal, Integer[]> payment) {
    final int paymentId = payment.value1();
    final int budgetId = payment.value2();
    final int userId = payment.value3();
    final String userName = getUserById(userId).getName();
    final String description = payment.value4();
    final double amount = payment.value5().doubleValue();
    final List<Integer> owingUserIds = Arrays.asList(payment.value6());
    return new Payment(paymentId, budgetId, userId, userName, description, amount, owingUserIds);
  }

  @Override
  public synchronized void settlePayments(int budgetId, List<Integer> paymentIds,
      List<BankTransfer> bankTransfers, boolean shouldSendEmails) {
    final int settlementId = dbContext
        .insertInto(SETTLEMENTS,
            SETTLEMENTS.BUDGET_ID)
        .values(budgetId)
        .returning(SETTLEMENTS.ID)
        .fetchOne()
        .getValue(SETTLEMENTS.ID);

    dbContext.transaction(configuration -> {
      final DSLContext transactionContext = DSL.using(configuration);
      insertPayments(transactionContext, settlementId, paymentIds);
      insertBankTransfers(transactionContext, settlementId, bankTransfers);
    });

    if (shouldSendEmails)
      new Thread(new BankTransferEmailSender(budgetId, bankTransfers)).start();

    Server.slh.unhangAll();
  }

  private synchronized void insertPayments(DSLContext transactionContext, int settlementId, List<Integer> paymentIds) {
    transactionContext
        .update(PAYMENTS)
        .set(PAYMENTS.SETTLED, true)
        .set(PAYMENTS.SETTLEMENT_ID, settlementId)
        .where(PAYMENTS.ID.in(paymentIds))
        .execute();
  }

  private synchronized void insertBankTransfers(DSLContext transactionContext, int settlementId,
      List<BankTransfer> bankTransfers) {
    for (BankTransfer transfer : bankTransfers) {
      transactionContext
          .insertInto(BANK_TRANSFERS,
              BANK_TRANSFERS.SETTLEMENT_ID,
              BANK_TRANSFERS.SENDER,
              BANK_TRANSFERS.RECIPIENT,
              BANK_TRANSFERS.AMOUNT
          )
          .values(settlementId, transfer.getSenderId(), transfer.getRecipientId(), transfer.getAmount())
          .execute();
    }
  }

  @Override
  public synchronized void removeParticipant(int budgetId, int userId) {
    dbContext
        .delete(USER_BUDGET)
        .where(USER_BUDGET.USER_ID.equal(userId))
        .and(USER_BUDGET.BUDGET_ID.equal(budgetId))
        .execute();

    Server.slh.unhangAll();
  }

  @Override
  public synchronized List<BankTransfer> getBankTransfersToSend(int userId) {
    return getBankTransfers(BANK_TRANSFERS.SENDER.equal(userId));
  }

  @Override
  public synchronized List<BankTransfer> getBankTransfersToReceive(int userId) {
    return getBankTransfers(BANK_TRANSFERS.RECIPIENT.equal(userId));
  }

  @Override
  public synchronized List<BankTransfer> getBankTransfersBySettlementId(int settlementId) {
    return getBankTransfers(BANK_TRANSFERS.SETTLEMENT_ID.equal(settlementId));
  }

  private synchronized List<BankTransfer> getBankTransfers(Condition condition) {
    return dbContext
        .select(
            BUDGETS.NAME,
            BANK_TRANSFERS.ID,
            BANK_TRANSFERS.SENDER,
            BANK_TRANSFERS.RECIPIENT,
            BANK_TRANSFERS.AMOUNT,
            BANK_TRANSFERS.STATUS
        )
        .from(BANK_TRANSFERS)
        .join(SETTLEMENTS)
        .on(SETTLEMENTS.ID.equal(BANK_TRANSFERS.SETTLEMENT_ID))
        .join(BUDGETS)
        .on(BUDGETS.ID.equal(SETTLEMENTS.BUDGET_ID))
        .where(condition)
        .fetch()
        .stream()
        .map(this::extractIntoBankTransfer)
        .collect(Collectors.toList());
  }

  private BankTransfer extractIntoBankTransfer(
      Record6<String, Integer, Integer, Integer, BigDecimal, Integer> bankTransfer) {
    final String budgetName = bankTransfer.value1();
    final int transferId = bankTransfer.value2();
    final User sender = getUserById(bankTransfer.value3());
    final User recipient = getUserById(bankTransfer.value4());
    final BigDecimal amount = bankTransfer.value5();
    final int status = bankTransfer.value6();
    return new BankTransfer(transferId, budgetName, sender, recipient, amount, status);
  }

  @Override
  public synchronized void setBankTransfersStatus(Map<Integer, BankTransfer.Status> bankTransfers) {
    dbContext.transaction(configuration -> {
      for (Entry<Integer, BankTransfer.Status> transferEntry : bankTransfers.entrySet()) {
        final int transferId = transferEntry.getKey();
        final Status status = transferEntry.getValue();
        DSL.using(configuration)
            .update(BANK_TRANSFERS)
            .set(BANK_TRANSFERS.STATUS, status.getValue())
            .where(BANK_TRANSFERS.ID.equal(transferId))
            .execute();
      }
    });
    Server.slh.unhangAll();
  }

  @Override
  public synchronized void setBankTransfersStatus(int transferId, int status) {
    dbContext
        .update(BANK_TRANSFERS)
        .set(BANK_TRANSFERS.STATUS, status)
        .where(BANK_TRANSFERS.ID.equal(transferId))
        .execute();
  }
}
