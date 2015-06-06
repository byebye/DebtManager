package client.windows;

import client.controllers.ParticipantDetailsController;
import common.Budget;
import common.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ParticipantDetailsWindow extends Stage {
   public ParticipantDetailsWindow(Budget budget, User participant, boolean hasUnaccountedPayments) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ParticipantDetailsWindow.fxml"));
      Parent root = fxmlLoader.load();
      ParticipantDetailsController controller = fxmlLoader.<ParticipantDetailsController>getController();

      controller.setStage(this);
      controller.setBudget(budget);
      controller.setParticipant(participant, hasUnaccountedPayments);

      setTitle(budget.getName() + " participant details");
      setScene(new Scene(root));
      initModality(Modality.WINDOW_MODAL);
   }
}