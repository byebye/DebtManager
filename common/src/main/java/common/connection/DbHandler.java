package common.connection;

import common.data.BankTransfer;
import common.data.Budget;
import common.data.Payment;
import common.data.Settlement;
import common.data.User;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface DbHandler extends Remote {

  User getUserByEmail(String email) throws RemoteException;

  User getUserById(int userId) throws RemoteException;

  boolean createUser(String email, String name, String bankAccount, String passwordHash) throws RemoteException;

  boolean createBudget(Budget budget) throws RemoteException;

  boolean deleteBudget(Budget budget) throws RemoteException;

  List<Budget> getAllBudgets(int userId) throws RemoteException;

  List<User> getBudgetParticipants(int budgetId) throws RemoteException;

  void addBudgetParticipants(int budgetId, List<User> users) throws RemoteException;

  void removeParticipant(int budgetId, int userId) throws RemoteException;

  void addPayment(Budget budget, int userId, BigDecimal amount, String description) throws RemoteException;

  void updatePayment(int paymentId, int userId, BigDecimal amount, String description) throws RemoteException;

  void deletePayment(int paymentId) throws RemoteException;

  List<Payment> getAllPayments(int budgetId, boolean settled) throws RemoteException;

  List<Payment> getPaymentsBySettlementId(int settlementId) throws RemoteException;

  void settlePayments(int budgetId, List<Payment> paymentsToSettle, List<BankTransfer> bankTransfers,
      boolean sendEmails
  ) throws RemoteException;

  List<Settlement> getAllSettlementsOfBudget(int budgetId) throws RemoteException;

  List<BankTransfer> getBankTransfersToSend(int userId) throws RemoteException;

  List<BankTransfer> getBankTransfersToReceive(int userId) throws RemoteException;

  List<BankTransfer> getBankTransfersBySettlementId(int settlementId) throws RemoteException;

  void setBankTransfersStatus(int transferId, int status) throws RemoteException;

  void setBankTransfersStatus(Map<Integer, BankTransfer.Status> bankTransfers) throws RemoteException;
}
