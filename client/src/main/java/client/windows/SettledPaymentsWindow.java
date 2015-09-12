package client.windows;

import common.data.Settlement;
import client.controllers.SettledPaymentsController;

import javafx.stage.Modality;

public class SettledPaymentsWindow extends DebtManagerWindow {

  public SettledPaymentsWindow(Settlement settlement) {
    super("/fxml/SettledPaymentsWindow.fxml", "DeptManager - Settled payments", Modality.APPLICATION_MODAL);

    SettledPaymentsController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setSettlement(settlement);
    controller.fillTableSettledPayments();
  }
}
