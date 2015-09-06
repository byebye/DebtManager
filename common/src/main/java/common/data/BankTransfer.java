package common.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankTransfer implements Serializable {

  private final int id;
  private final User sender;
  private final User recipient;
  private final BigDecimal amount;
  private final String budgetName;
  private Status status;
  private boolean toUpdate = false;

  public BankTransfer(User sender, User recipient, BigDecimal amount) {
    this(-1, null, sender, recipient, amount, 0);
  }

  public BankTransfer(int id, String budgetName, User sender, User recipient, BigDecimal amount, int status) {
    this.id = id;
    this.budgetName = budgetName;
    this.sender = sender;
    this.recipient = recipient;
    this.amount = amount;
    this.status = Status.fromValue(status);
  }

  public void updateStatus(int updatingUser) {
    if (updatingUser == sender.getId()) {
      status = Status.NotConfirmed;
    }
    else {
      // budget owner or user receiving transfer
      status = Status.Confirmed;
    }
  }

  public int getId() {
    return id;
  }

  public String getBudgetName() {
    return budgetName;
  }

  public Status getStatus() {
    return status;
  }

  public String getSender() {
    return sender.getName();
  }

  public int getSenderId() {
    return sender.getId();
  }

  public String getRecipient() {
    return recipient.getName();
  }

  public int getRecipientId() {
    return recipient.getId();
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public BankAccount getBankAccount() {
    return recipient.getBankAccount();
  }

  public void setToUpdate(boolean v) {
    toUpdate = v;
  }

  public boolean isToUpdate() {
    return toUpdate;
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
