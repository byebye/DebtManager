package tcs.javaproject.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tcs.javaproject.Budget;
import tcs.javaproject.User;
import tcs.javaproject.controllers.ParticipantDetailsController;

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