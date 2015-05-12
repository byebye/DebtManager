package tcs.javaproject.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tcs.javaproject.controllers.AddUserToBudgetController;

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
