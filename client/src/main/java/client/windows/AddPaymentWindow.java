package client.windows;

import client.controllers.AddPaymentController;
import common.data.Budget;
import common.data.User;
import javafx.collections.ObservableList;
import javafx.stage.Modality;

public class AddPaymentWindow extends DebtManagerWindow {

  private AddPaymentController controller;

  public AddPaymentWindow(Budget budget, ObservableList<User> participants) {
    super("/fxml/PaymentWindow.fxml", "DeptManager - add payment", Modality.WINDOW_MODAL,
        param -> new AddPaymentController());

    controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setBudget(budget);
    controller.initParticipantsList(participants);
  }

  public AddPaymentController getController() {
    return controller;
  }
}
