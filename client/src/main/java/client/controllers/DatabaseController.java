package client.controllers;

import common.*;

import java.math.BigDecimal;
import java.util.List;

public class DatabaseController implements DBHandler {
   public boolean validateUserPassword(Email email, String passwordHash) {
      return false;
   }

   public User getUserByEmail(String email) {
      return null;
   }

   public User getUserById(int userId) {
      return null;
   }

   public boolean createUser(User user, String passwordHash) {
      return false;
   }

   public boolean createBudget(Budget budget) {
      return false;
   }

   public boolean deleteBudget(Budget budget) {
      return false;
   }

   public List<Budget> getAllBudgets(int userId) {
      return null;
   }

   public List<User> getBudgetParticipants(int budgetId) {
      return null;
   }

   public void addBudgetParticipants(int budgetId, List<User> users) {

   }

   public void addPayment(Budget budget, int userId, BigDecimal amount, String what) {

   }

   public void updatePayment(int paymentId, int userId, BigDecimal amount, String what) {

   }

   public void deletePayment(int paymentId) {

   }

   public List<Payment> getAllPayments(int budgetId, boolean accounted) {
      return null;
   }

   public List<BankTransfer> calculateBankTransfers(int budgetId) {
      return null;
   }

   public void settleUnaccountedPayments(int budgetId) {

   }

   public void removeParticipant(int budgetId, int userId) {

   }
}
