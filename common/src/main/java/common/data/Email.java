package common.data;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Email implements Serializable {

  public final static int MAX_LENGTH = 120;

  private static final String emailValidationPattern =
      "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
  private final String address;

  public Email(String address) {
    if (!isValid(address)) {
      throw new IllegalArgumentException("Invalid email address: " + address);
    }
    this.address = address;
  }

  public static boolean isValid(String address) {
    return Pattern.matches(emailValidationPattern, address);
  }

  @Override
  public String toString() {
    return address;
  }
}
