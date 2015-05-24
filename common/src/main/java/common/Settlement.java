package common;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by vsmasster on 24.05.15.
 */
public class Settlement implements Serializable{
   private int budgetId, settlementId, numPaidBankTransfers, numAllBankTransfers;
   private double amount;
   private String date,status;

   public Settlement(int settlementId,int budgetId,int numPaidBankTransfers,int numAllBankTransfers,String date,double amount){
      this.budgetId = budgetId;
      this.date = date;
      this.numAllBankTransfers = numAllBankTransfers;
      this.numPaidBankTransfers = numPaidBankTransfers;
      this.settlementId = settlementId;
      this.amount = amount;
      this.status = numPaidBankTransfers + "/" + numAllBankTransfers;
   }

   public int getBudgetId(){return budgetId;}
   public int getSettlementId(){return settlementId;}
   public int getNumPaidBankTransfers(){return numPaidBankTransfers;}
   public int getNumAllBankTransfers(){return numAllBankTransfers;}
   public int getNumNotPaidBankTransfers(){return numAllBankTransfers-numPaidBankTransfers;}
   public String getDate(){return date;}
   public String getStatus(){return status;}
   public double getAmount(){return amount;}


}
