package common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DBHandler extends Remote {
   boolean validateUserPassword(Email email, String passwordHash) throws RemoteException;
   User getUserByEmail(String email) throws RemoteException;
   User getUserById(int userId) throws RemoteException;
   boolean createUser(User user, String passwordHash) throws RemoteException;
   boolean createUser(String email, String name, BigInteger bankAccount, String passwordHash) throws RemoteException;
   boolean createBudget(Budget budget) throws RemoteException;
   boolean deleteBudget(Budget budget) throws RemoteException;
   List<Budget> getAllBudgets(int userId) throws RemoteException;
   List<User> getBudgetParticipants(int budgetId) throws RemoteException;
   void addBudgetParticipants(int budgetId, List<User> users) throws RemoteException;
   void addPayment(Budget budget, int userId, BigDecimal amount, String what) throws RemoteException;
   void updatePayment(int paymentId, int userId, BigDecimal amount, String what) throws RemoteException;
   void deletePayment(int paymentId) throws RemoteException;
   List<Payment> getAllPayments(int budgetId, boolean accounted) throws RemoteException;
   List<BankTransfer> calculateBankTransfers(int budgetId, List<Payment> paymentsToSettle) throws RemoteException;
   void settleUnaccountedPayments(int budgetId, List<Payment> paymentsToSettle,List<BankTransfer> bankTransfers) throws RemoteException;
   void removeParticipant(int budgetId, int userId) throws RemoteException;
   List<Settlement> getAllSettlements(int budgetId) throws RemoteException;
   List<Payment> getPaymentsBySettlementId(int settlementId) throws RemoteException;
   List<BankTransfer> getMyBankTransfers(int userId) throws RemoteException;
   List<BankTransfer> getOthersBankTransfers(int userId) throws RemoteException;
   List<BankTransfer> getBankTransfersBySettlementId(int settlementId) throws RemoteException;
   void setBankTransferStatus(List<Integer> bankTransfers,int status) throws RemoteException;
   void updateBankTransferStatus(int bankTransferId,int userId) throws RemoteException;
}
