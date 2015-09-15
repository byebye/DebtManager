package common.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class Settlement implements Serializable {

  private final int budgetId;
  private final int settlementId;
  private final int paidBankTransfersCount;
  private final int allBankTransfersCount;
  private final BigDecimal spentMoney;
  private final Date settleDate;

  public Settlement(int settlementId, int budgetId, int paidBankTransfersCount, int allBankTransfersCount,
      Date settleDate,
      BigDecimal spentMoney
  ) {
    this.budgetId = budgetId;
    this.settleDate = settleDate;
    this.allBankTransfersCount = allBankTransfersCount;
    this.paidBankTransfersCount = paidBankTransfersCount;
    this.settlementId = settlementId;
    this.spentMoney = spentMoney.setScale(2);
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

  public String getStatusString() {
    return paidBankTransfersCount + "/" + allBankTransfersCount;
  }

  public boolean areAllBankTransfersPaid() {
    return paidBankTransfersCount == allBankTransfersCount;
  }

  public BigDecimal getSpentMoney() {
    return spentMoney;
  }
}
