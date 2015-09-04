package client.windows;

import client.controllers.BudgetCreatorController;

import javafx.stage.Modality;

public class BudgetCreatorWindow extends DebtManagerWindow {

  public BudgetCreatorWindow() {
    super("/fxml/BudgetCreatorWindow.fxml", "DeptManager - Budget Creator", Modality.APPLICATION_MODAL);

    BudgetCreatorController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
  }
}
