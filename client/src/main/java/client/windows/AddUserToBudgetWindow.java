package client.windows;

import client.controllers.AddUserToBudgetController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddUserToBudgetWindow extends Stage {
   public AddUserToBudgetWindow(BudgetWindow budgetWindow) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddUserToBudgetWindow.fxml"));
      Parent root = fxmlLoader.load();
      AddUserToBudgetController controller = fxmlLoader.<AddUserToBudgetController>getController();
      controller.setParent(budgetWindow);
      setTitle("DeptManager - add user");
      setScene(new Scene(root));
   }
}
