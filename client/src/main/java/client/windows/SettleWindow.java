package client.windows;

import client.controllers.BudgetController;
import client.controllers.SettleController;
import common.data.Budget;
import common.data.Payment;
import javafx.collections.ObservableList;
import javafx.stage.Modality;

import java.util.List;

public class SettleWindow extends DebtManagerWindow {

  public SettleWindow(BudgetController budgetController, Budget budget, List<Payment> paymentsToSettle) {
    super("/fxml/SettleWindow.fxml", "DeptManager - settle " + budget.getName(), Modality.WINDOW_MODAL);

    SettleController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setBudgetController(budgetController);
    controller.setBudget(budget);
    controller.setPaymentsToSettle(paymentsToSettle);
    controller.fillBankTransfersTable();
  }
}
