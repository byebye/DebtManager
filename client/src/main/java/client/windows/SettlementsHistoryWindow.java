package client.windows;

import common.data.Budget;
import client.controllers.SettlementsHistoryController;

import javafx.stage.Modality;

public class SettlementsHistoryWindow extends DebtManagerWindow {

  public SettlementsHistoryWindow(Budget budget) {
    super("/fxml/SettlementsHistoryWindow.fxml", "DeptManager - settlements history", Modality.WINDOW_MODAL);

    SettlementsHistoryController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setBudget(budget);
    controller.fillTableSettlementsHistory();
  }
}
