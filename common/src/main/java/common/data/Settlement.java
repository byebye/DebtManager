package common.data;

import java.io.Serializable;
import java.sql.Date;

public class Settlement implements Serializable {

  private final int budgetId;
  private final int settlementId;
  private final int paidBankTransfersCount;
  private final int allBankTransfersCount;
  private final double amount;
  private final Date settleDate;
  private final String status;

  public Settlement(int settlementId, int budgetId, int paidBankTransfersCount, int allBankTransfersCount,
      Date settleDate,
      double amount
  ) {
    this.budgetId = budgetId;
    this.settleDate = settleDate;
    this.allBankTransfersCount = allBankTransfersCount;
    this.paidBankTransfersCount = paidBankTransfersCount;
    this.settlementId = settlementId;
    this.amount = amount;
    if (paidBankTransfersCount != allBankTransfersCount)
      this.status = paidBankTransfersCount + "/" + allBankTransfersCount;
    else
      this.status = "OK";
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

  public Date getSettleDate() {
    return settleDate;
  }

  public String getStatus() {
    return status;
  }

  public double getAmount() {
    return amount;
  }
}
