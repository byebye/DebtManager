package common;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankTransfer implements Serializable {
   private User who, whom;
   private BigDecimal amount;
   private String budgetName;
   private String status;
   private String whoAcc;
   private int id;
   private boolean accept = false;

   public BankTransfer(User who, User whom, BigDecimal amount) {
      this.who = who;
      this.whom = whom;
      this.amount = amount;
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

   public BankTransfer(int id,User who, User whom, BigDecimal amount, String status){
      this.who = who;
      this.whom = whom;
      this.amount = amount;
      this.status = status;
      this.id = id;
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

   public void setAccept(boolean v){
      accept = v;
   }
   public boolean getAccept(){
      return accept;
   }

   public int getId(){
      return id;
   }
}
