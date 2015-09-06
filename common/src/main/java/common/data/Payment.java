package common.data;

import java.io.Serializable;

public class Payment implements Serializable {

  public static final int MAX_DESCRIPTION_LENGTH = 200;

  private final int id;
  private final String payer;
  private final String description;
  private final int userId;
  private final int budgetId;
  private final double amount;
  private boolean accept = true;

  public Payment(int id, int budgetId, int userId, String payer, String description, double amount) {
    this.id = id;
    this.budgetId = budgetId;
    this.userId = userId;
    this.payer = payer;
    this.description = description;
    this.amount = amount;
  }

  public int getBudgetId() {
    return budgetId;
  }

  public int getUserId() {
    return userId;
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
}
