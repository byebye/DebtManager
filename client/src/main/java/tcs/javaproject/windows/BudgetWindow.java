package tcs.javaproject.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tcs.javaproject.controllers.BudgetController;

import java.io.IOException;

public class BudgetWindow extends Stage {
   BudgetController controller;

   public BudgetController getController() {
      return controller;
   }

   public BudgetWindow(Budget budget, int userId) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetWindow.fxml"));
      Parent root = fxmlLoader.load();
      controller = fxmlLoader.<BudgetController>getController();
      controller.setBudget(budget, userId, this);
      controller.fillAllTables();
      setTitle("DeptManager - " + budget.getName());
      setScene(new Scene(root));
   }
}

