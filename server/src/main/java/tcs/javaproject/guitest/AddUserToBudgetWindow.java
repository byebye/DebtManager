package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddUserToBudgetWindow extends Stage {
   public AddUserToBudgetWindow(int budgetId, BudgetWindow budgetWindow) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddUserToBudgetWindow.fxml"));
      Parent root = fxmlLoader.load();
      AddUserToBudgetController controller = fxmlLoader.<AddUserToBudgetController>getController();
      controller.setData(budgetId, budgetWindow);

      setTitle("DeptManager - add user");
      setScene(new Scene(root));
   }
}