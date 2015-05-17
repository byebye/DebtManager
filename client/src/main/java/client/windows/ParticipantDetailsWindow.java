package client.windows;

import client.controllers.ParticipantDetailsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import common.Budget;
import common.User;

import java.io.IOException;

public class ParticipantDetailsWindow extends Stage {
   public ParticipantDetailsWindow(Budget budget, User participant) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ParticipantDetailsWindow.fxml"));
      Parent root = fxmlLoader.load();
      ParticipantDetailsController controller = fxmlLoader.<ParticipantDetailsController>getController();
      controller.setBudgetId(budget.getId());
      controller.setParticipant(participant);
      setTitle(budget.getName() + " participant details");
      setScene(new Scene(root));
   }
}