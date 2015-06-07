package client.windows;

import client.controllers.BudgetController;
import common.Budget;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetWindow extends Stage {

   private BudgetController controller;

   public BudgetController getController() {
      return controller;
   }

   public BudgetWindow(Budget budget) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetWindow.fxml"));
      Parent root = fxmlLoader.load();
      controller = fxmlLoader.<BudgetController>getController();

      controller.setStage(this);
      controller.setBudget(budget);
      controller.update();

      setTitle("DeptManager - " + budget.getName());
      setScene(new Scene(root));
      initModality(Modality.APPLICATION_MODAL);
   }
}

