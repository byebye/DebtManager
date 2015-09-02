package client.windows;

import client.controllers.BankTransfersController;
import javafx.stage.Modality;

public class BankTransfersWindow extends DebtManagerWindow {

  public BankTransfersWindow() {
    super("/fxml/BankTransfersWindow.fxml", "DeptManager - Manage bank transfers", Modality.APPLICATION_MODAL);

    BankTransfersController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.subscribeForUpdates();
  }
}
