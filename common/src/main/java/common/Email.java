package common;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Created by glapul on 12.05.15.
 */
public class Email implements Serializable {

   private final String address;

   public Email(String address) {
      if (!isValid(address))
         throw new IllegalArgumentException();
      this.address = address;
   }

   public static boolean isValid(String address) {
      String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
      return Pattern.matches(ePattern, address);
   }

   public String getAddress() {
      return address;
   }
}
