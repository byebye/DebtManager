package common;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankTransfer implements Serializable {
   private User who, whom;
   private BigDecimal amount;
   private int paymentId;
   private String budgetName;
   private String status;
   private String whoAcc;

   public BankTransfer(User who, User whom, BigDecimal amount,int paymentId) {
      this.who = who;
      this.whom = whom;
      this.amount = amount;
      this.paymentId = paymentId;
   }

   public BankTransfer(String budgetName, User who, BigDecimal amount, String status){
      this.budgetName = budgetName;
      this.who = who;
      this.amount = amount;
      this.status = status;
      this.whoAcc =  who.getName()+" ("+who.getBankAccount()+")";
      System.out.println(whoAcc);
      System.out.println(who.getName());
   }

   public String getBudgetName(){return budgetName;}
   public String getStatus(){return status;}
   public String getWhoAcc(){return whoAcc;}

   public String getWho() {
      return who.getName();
   }
   public int getWhoId(){return who.getId();}

   public String getWhom() {
      return whom.getName();
   }
   public int getWhomId(){return whom.getId();}

   public BigDecimal getAmount() {
      return amount;
   }

   public String getBankAccount() {
      return whom.getBankAccount();
   }

   public int getPaymentId(){
      return paymentId;
   }
}
