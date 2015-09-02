package common.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankTransfer implements Serializable {

  private User who, whom;
  private BigDecimal amount;
  private String budgetName;
  private Status status;
  private int id;
  private boolean toUpdate = false;

  public BankTransfer(User who, User whom, BigDecimal amount) {
    this.who = who;
    this.whom = whom;
    this.amount = amount;
  }

  public BankTransfer(int id, String budgetName, User who, User whom, BigDecimal amount, int status) {
    this.id = id;
    this.budgetName = budgetName;
    this.who = who;
    this.whom = whom;
    this.amount = amount;
    this.status = Status.fromValue(status);
  }

  public String getBudgetName() {
    return budgetName;
  }

  public void updateStatus(int updatingUser) {
    if (updatingUser == who.getId()) {
      status = Status.NotConfirmed;
    }
    else {
      // budget owner or user receiving transfer
      status = Status.Confirmed;
    }
  }

  public Status getStatus() {
    return status;
  }

  public String getWho() {
    return who.getName();
  }

  public int getWhoId() {
    return who.getId();
  }

  public String getWhom() {
    return whom.getName();
  }

  public int getWhomId() {
    return whom.getId();
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getBankAccount() {
    return whom.getBankAccount();
  }

  public void setToUpdate(boolean v) {
    toUpdate = v;
  }

  public boolean isToUpdate() {
    return toUpdate;
  }

  public int getId() {
    return id;
  }

  public enum Status {
    NotPaid(0),
    NotConfirmed(1),
    Confirmed(2);

    private final int value;

    Status(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public static Status fromValue(int value) {
      for (Status status : values()) {
        if (status.value == value)
          return status;
      }
      return NotPaid;
    }
  }
}
