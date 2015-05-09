package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Vsmasster on 09.05.15.
 */
public class AddUserWindow extends Stage {
   public AddUserWindow(int budgetId, BudgetWindow budgetWindow) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddUserWindow.fxml"));
      Parent root = fxmlLoader.load();
      AddUserController controller = fxmlLoader.<AddUserController>getController();
      controller.setData(budgetId,budgetWindow);

      setTitle("DeptManager - add user");
      setScene(new Scene(root));
   }
}
