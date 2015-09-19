package common.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Payment implements Serializable {

  public static final int MAX_DESCRIPTION_LENGTH = 200;

  private final int id;
  private final String payer;
  private final String description;
  private final int payerId;
  private final int budgetId;
  private final double amount;
  private boolean accept = true;
  private final Set<Integer> owingUsers = new HashSet<>();

  public Payment(int id, int budgetId, int payerId, String payer, String description, double amount,
      Collection<Integer> owingUsers) {
    this.id = id;
    this.budgetId = budgetId;
    this.payerId = payerId;
    this.payer = payer;
    this.description = description;
    this.amount = amount;
    this.owingUsers.addAll(owingUsers);
  }

  public int getBudgetId() {
    return budgetId;
  }

  public int getPayerId() {
    return payerId;
  }

  public String getPayer() {
    return payer;
  }

  public String getDescription() {
    return description;
  }

  public double getAmount() {
    return amount;
  }

  public int getId() {
    return id;
  }

  public boolean isAccepted() {
    return accept;
  }

  public void setAccept(boolean c) {
    accept = c;
  }

  public Set<Integer> getOwingUsers() {
    return owingUsers;
  }

  public void addOwingUser(int userId) {
    owingUsers.add(userId);
  }

  public void addOwingUsers(Collection<Integer> userIds) {
    owingUsers.addAll(userIds);
  }

  public void removeOwingUser(int userId) {
    owingUsers.remove(userId);
  }

  public boolean isUserOwing(int userId) {
    return owingUsers.contains(userId);
  }
}
