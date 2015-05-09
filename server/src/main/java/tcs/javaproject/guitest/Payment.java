package tcs.javaproject.guitest;

public class Payment {
   private String who, what;
   private int id;
   private double amount;

   public Payment(String who, String what, double amount, int id) {
      this.who = who;
      this.what = what;
      this.amount = amount;
      this.id = id;
   }

   public String getWho() {
      return who;
   }

   public String getWhat() {
      return what;
   }

   public double getAmount() {
      return amount;
   }

   public int getId() {
      return id;
   }
}
