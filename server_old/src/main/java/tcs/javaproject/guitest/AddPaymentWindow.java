package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPaymentWindow extends Stage {

   AddPaymentController controller;

   public AddPaymentController getController() {
      return controller;
   }

   public AddPaymentWindow(Budget budget, int userId) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddPaymentWindow.fxml"));
      Parent root = fxmlLoader.load();
      controller = fxmlLoader.<AddPaymentController>getController();
      controller.setBudget(budget);
      controller.setUser(userId);
      setTitle("DeptManager - " + budget.getName());
      setScene(new Scene(root));
   }
}
