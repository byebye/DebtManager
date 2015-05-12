package tcs.javaproject.guitest;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;

public class BankTransfer {
   private User who, whom;
   private BigDecimal amount;
   private Boolean accept;
   private int paymentId;

   public BankTransfer(User who, User whom, BigDecimal amount,int paymentId) {
      this.who = who;
      this.whom = whom;
      this.amount = amount;
      this.accept = true;
      this.paymentId = paymentId;
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

   public Boolean getAccept(){return accept;}

   public void setAccept(Boolean c){
      accept = c;
   }

   public int getPaymentId(){
      return paymentId;
   }
}
