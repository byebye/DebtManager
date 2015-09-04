package client.windows;

import common.data.Budget;
import common.data.Payment;
import common.data.User;
import client.controllers.UpdatePaymentController;

import javafx.collections.ObservableList;
import javafx.stage.Modality;

public class UpdatePaymentWindow extends DebtManagerWindow {

  public UpdatePaymentWindow(Budget budget, Payment payment, ObservableList<User> participants) {
    super("/fxml/PaymentWindow.fxml", "DeptManager - payment details", Modality.WINDOW_MODAL,
        param -> new UpdatePaymentController());

    UpdatePaymentController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setBudget(budget);
    controller.setPayment(payment);
    controller.initParticipantsList(participants);
  }
}
