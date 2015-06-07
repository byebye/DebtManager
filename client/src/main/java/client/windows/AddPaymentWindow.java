package client.windows;

import client.controllers.AddPaymentController;
import common.Budget;
import common.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPaymentWindow extends Stage {

   AddPaymentController controller;

   public AddPaymentController getController() {
      return controller;
   }

   public AddPaymentWindow(Budget budget, ObservableList<User> participants) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddPaymentWindow.fxml"));
      Parent root = fxmlLoader.load();
      controller = fxmlLoader.<AddPaymentController>getController();

      controller.setStage(this);
      controller.setBudget(budget);
      controller.setParticipantsList(participants);

      setTitle("DeptManager - add new payment");
      setScene(new Scene(root));
      initModality(Modality.WINDOW_MODAL);
   }
}
