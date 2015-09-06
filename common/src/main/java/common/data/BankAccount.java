package common.data;

import java.io.Serializable;
import java.util.regex.Pattern;

public class BankAccount implements Serializable {

  private final String accountNumber;

  public BankAccount(String accountNumber) {
    if (!isValid(accountNumber)) {
      throw new IllegalArgumentException("Invalid account number: " + accountNumber);
    }
    this.accountNumber = accountNumber.replaceAll("\\s", "");
  }

  public static boolean isValid(String accountNumber) {
    return Pattern.matches("\\d{22}", accountNumber.replaceAll("\\s", ""));
  }

  @Override
  public String toString() {
    return accountNumber;
  }
}
