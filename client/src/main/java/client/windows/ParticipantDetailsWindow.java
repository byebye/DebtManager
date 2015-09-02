package client.windows;

import client.controllers.ParticipantDetailsController;
import common.data.Budget;
import common.data.User;
import javafx.stage.Modality;

public class ParticipantDetailsWindow extends DebtManagerWindow {

  public ParticipantDetailsWindow(Budget budget, User participant, boolean hasUnaccountedPayments) {
    super("/fxml/ParticipantDetailsWindow.fxml", budget.getName() + " participant details", Modality.WINDOW_MODAL);

    ParticipantDetailsController controller = fxmlLoader.getController();
    controller.setCurrentStage(this);
    controller.setBudget(budget);
    controller.setParticipant(participant, hasUnaccountedPayments);
  }
}