package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetWindow extends Stage {

   public BudgetWindow(Budget budget, int userId) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetWindow.fxml"));
      Parent root = fxmlLoader.load();
      BudgetController controller = fxmlLoader.<BudgetController>getController();
      controller.setBudget(budget,userId,this);
      controller.fillTabUnaccPayments();
      setTitle("DeptManager - " + budget.getName());
      setScene(new Scene(root));
   }
}

