package common.data;

import java.io.Serializable;
import java.util.regex.Pattern;

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
    return Pattern.matches("\\d{22}", accountNumber.replaceAll("\\s", ""));
  }
}
