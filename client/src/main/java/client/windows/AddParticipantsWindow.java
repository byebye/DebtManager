package client.windows;

import client.controllers.AddParticipantsController;
import client.controllers.BudgetController;
import javafx.stage.Modality;

public class AddParticipantsWindow extends DebtManagerWindow {

  public AddParticipantsWindow(BudgetController budgetController) {
    super("/fxml/AddParticipantsWindow.fxml", "DeptManager - add user", Modality.WINDOW_MODAL);

    AddParticipantsController controller = fxmlLoader.getController();
    controller.setBudgetController(budgetController);
    controller.setCurrentStage(this);
  }
}
