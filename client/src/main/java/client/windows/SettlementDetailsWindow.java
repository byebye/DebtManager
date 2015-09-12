package client.windows;

import common.data.Budget;
import common.data.Settlement;
import client.controllers.SettlementDetailsController;

import javafx.stage.Modality;

public class SettlementDetailsWindow extends DebtManagerWindow {

  public SettlementDetailsWindow(Settlement settlement, Budget budget) {
    super("/fxml/SettlementDetailsWindow.fxml", "DeptManager - show settlement details", Modality.WINDOW_MODAL);

    SettlementDetailsController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setData(settlement, budget);
    controller.fillSettlementsTable();
  }
}
