package common.utils;

import common.data.BankTransfer;
import common.data.BankTransfer.Status;

import java.util.Collections;
import java.util.List;

public class DataUtils {

  public static void sortTransfersByStatus(List<BankTransfer> transfers) {
    int it = 0;
    for (int i = 0; i < transfers.size(); i++) {
      if (transfers.get(i).getStatus() == Status.NotPaid)
        Collections.swap(transfers, it++, i);
    }
    for (int i = 0; i < transfers.size(); i++) {
      if (transfers.get(i).getStatus() == Status.NotConfirmed)
        Collections.swap(transfers, it++, i);
    }
  }
}
