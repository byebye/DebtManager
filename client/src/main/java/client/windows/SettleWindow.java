package client.windows;

import common.data.Budget;
import common.data.Payment;
import common.data.User;
import client.controllers.BudgetController;
import client.controllers.SettleController;

import javafx.stage.Modality;
import java.util.List;

public class SettleWindow extends DebtManagerWindow {

  public SettleWindow(BudgetController budgetController, Budget budget, List<User> participants,
      List<Payment> paymentsToSettle) {
    super("/fxml/SettleWindow.fxml", "DeptManager - settle " + budget.getName(), Modality.WINDOW_MODAL);

    SettleController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setBudgetController(budgetController);
    controller.setBudget(budget);
    controller.setParticipants(participants);
    controller.setPaymentsToSettle(paymentsToSettle);
    controller.fillBankTransfersTable();
  }
}
