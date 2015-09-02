package client.windows;

import client.controllers.BudgetsListController;
import javafx.stage.Modality;

public class BudgetsListWindow extends DebtManagerWindow {

  public BudgetsListWindow() {
    super("/fxml/BudgetsListWindow.fxml", "DeptManager - My Budgets", Modality.APPLICATION_MODAL);

    BudgetsListController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.update();
    controller.subscribeForUpdates();
  }
}
