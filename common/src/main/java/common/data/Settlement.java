package common.data;

import java.io.Serializable;

public class Settlement implements Serializable {

  private final int budgetId;
  private final int settlementId;
  private final int paidBankTransfersCount;
  private final int allBankTransfersCount;
  private final double amount;
  private final String date;
  private final String status;

  public Settlement(int settlementId, int budgetId, int paidBankTransfersCount, int allBankTransfersCount, String date,
      double amount
  ) {
    this.budgetId = budgetId;
    this.date = date;
    this.allBankTransfersCount = allBankTransfersCount;
    this.paidBankTransfersCount = paidBankTransfersCount;
    this.settlementId = settlementId;
    this.amount = amount;
    if (paidBankTransfersCount != allBankTransfersCount) this.status = paidBankTransfersCount + "/" + allBankTransfersCount;
    else this.status = "OK";
  }

  public int getBudgetId() {
    return budgetId;
  }

  public int getSettlementId() {
    return settlementId;
  }

  public int getPaidBankTransfersCount() {
    return paidBankTransfersCount;
  }

  public int getAllBankTransfersCount() {
    return allBankTransfersCount;
  }

  public int getNotPaidBankTransfersCount() {
    return allBankTransfersCount - paidBankTransfersCount;
  }

  public String getDate() {
    return date;
  }

  public String getStatus() {
    return status;
  }

  public double getAmount() {
    return amount;
  }
}
