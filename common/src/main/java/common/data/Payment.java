package common.data;

import java.io.Serializable;
import java.sql.Date;

public class Payment implements Serializable {
   private String who, what;
   private int id;
   private int userId;
   private int budgetId;
   private boolean accept = true;
   private double amount;
   private Date date;

   public Payment(int id, int budgetId, int userId, String who, String what, double amount, Date date) {
      this.id = id;
      this.budgetId = budgetId;
      this.userId = userId;
      this.who = who;
      this.what = what;
      this.amount = amount;
      this.date = date;
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

   public boolean isAccepted(){
      return accept;
   }

   public void setAccept(boolean c){
      accept = c;
   }

   public Date getDate() {
      return date;
   }
}
