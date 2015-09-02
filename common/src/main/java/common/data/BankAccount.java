package common.data;

import java.io.Serializable;

public class BankAccount implements Serializable {

  private final String accountNumber;

  public BankAccount(String accountNumber) {
    if (!isValid(accountNumber)) {
      throw new IllegalArgumentException("Invalid account number");
    }
    this.accountNumber = accountNumber.replaceAll("\\s", "");
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public static boolean isValid(String accountNumber) {
    return accountNumber.replaceAll("\\s", "").matches("\\d{22}");
  }
}
