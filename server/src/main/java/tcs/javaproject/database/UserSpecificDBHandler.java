package tcs.javaproject.database;

import tcs.javaproject.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by glapul on 16.05.15.
 */
public class UserSpecificDBHandler implements DBHandler {
    @Override
    public boolean validateUserPassword(Email email, String passwordHash) {
        return false;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public User getUserById(int userId) {
        return null;
    }

    @Override
    public boolean createUser(User user, String passwordHash) {
        return false;
    }

    @Override
    public boolean createBudget(Budget budget) {
        return false;
    }

    @Override
    public boolean deleteBudget(Budget budget) {
        return false;
    }

    @Override
    public List<Budget> getAllBudgets(int userId) {
        return null;
    }

    @Override
    public List<User> getBudgetParticipants(int budgetId) {
        return null;
    }

    @Override
    public void addBudgetParticipants(int budgetId, List<User> users) {

    }

    @Override
    public void addPayment(Budget budget, int userId, BigDecimal amount, String what) {

    }

    @Override
    public void updatePayment(int paymentId, int userId, BigDecimal amount, String what) {

    }

    @Override
    public void deletePayment(int paymentId) {

    }

    @Override
    public List<Payment> getAllPayments(int budgetId, boolean accounted) {
        return null;
    }

    @Override
    public List<BankTransfer> calculateBankTransfers(int budgetId) {
        return null;
    }

    @Override
    public void settleUnaccountedPayments(int budgetId) {

    }

    @Override
    public void removeParticipant(int budgetId, int userId) {

    }
}
