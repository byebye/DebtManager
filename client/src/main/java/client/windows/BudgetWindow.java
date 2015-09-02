package client.windows;

import client.controllers.BudgetController;
import common.data.Budget;
import javafx.stage.Modality;

public class BudgetWindow extends DebtManagerWindow {

  private BudgetController controller;

  public BudgetWindow(Budget budget) {
    super("/fxml/BudgetWindow.fxml", "DeptManager - " + budget.getName(), Modality.APPLICATION_MODAL);

    controller = fxmlLoader.<BudgetController>getController();
    controller.setCurrentStage(this);
    controller.setBudget(budget);
    controller.update();
    controller.subscribeForUpdates();
  }

  public BudgetController getController() {
    return controller;
  }
}

