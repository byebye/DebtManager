package tcs.javaproject.guitest;

public class Payment {
   private String who, what;
   private int id;
   private int userId;
   private int budgetId;
   private boolean accept = true;
   private double amount;

   public Payment(int budgetId, int userId, String who, String what, double amount, int id) {
      this.budgetId = budgetId;
      this.userId = userId;
      this.who = who;
      this.what = what;
      this.amount = amount;
      this.id = id;
   }

   public int getBudgetId() {
      return budgetId;
   }

   public int getUserId() {
      return userId;
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

   public boolean getAccept(){
      return accept;
   }

   public void setAccept(boolean c){
      accept = c;
   }
}
