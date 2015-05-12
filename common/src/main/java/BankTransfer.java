import java.math.BigDecimal;

public class BankTransfer {
   private User who, whom;
   private BigDecimal amount;

   public BankTransfer(User who, User whom, BigDecimal amount) {
      this.who = who;
      this.whom = whom;
      this.amount = amount;
   }

   public String getWho() {
      return who.getName();
   }

   public String getWhom() {
      return whom.getName();
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public String getBankAccount() {
      return whom.getBankAccount();
   }
}
