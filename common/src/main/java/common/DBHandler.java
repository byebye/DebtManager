package common;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.util.List;

public interface DBHandler extends Remote {
   boolean validateUserPassword(Email email, String passwordHash);
   User getUserByEmail(String email);
   User getUserById(int userId);
   boolean createUser(User user, String passwordHash);
   boolean createBudget(Budget budget);
   boolean deleteBudget(Budget budget);
   List<Budget> getAllBudgets(int userId);
   List<User> getBudgetParticipants(int budgetId);
   void addBudgetParticipants(int budgetId, List<User> users);
   void addPayment(Budget budget, int userId, BigDecimal amount, String what);
   void updatePayment(int paymentId, int userId, BigDecimal amount, String what);
   void deletePayment(int paymentId);
   List<Payment> getAllPayments(int budgetId, boolean accounted);
   List<BankTransfer> calculateBankTransfers(int budgetId);
   void settleUnaccountedPayments(int budgetId);
   void removeParticipant(int budgetId, int userId);
}
