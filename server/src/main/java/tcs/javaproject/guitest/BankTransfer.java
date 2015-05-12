package tcs.javaproject.guitest;

import java.math.BigDecimal;

public class BankTransfer {
   private User who, whom;
   private BigDecimal amount;

   public BankTransfer(User who, User whom, BigDecimal amount) {
      this.who = who;
      this.whom = whom;
      this.amount = amount;
   }

   public User getWho() {
      return who;
   }

   public User getWhom() {
      return whom;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public String getBankAccount() {
      return whom.getBankAccount();
   }
}
