package common;

import java.io.Serializable;

/**
 * Created by glapul on 12.05.15.
 */
public class IBAN implements Serializable {

   private final String IBAN;

   IBAN(String iban) {
      if (!validate(iban))
         throw new IllegalArgumentException();

      this.IBAN = normalize(iban);
   }

   private boolean validate(String iban) {
      return true;
   }

   private String normalize(String iban) {
      return iban;
   }

   public String getIBAN() {
      return IBAN;
   }
}
