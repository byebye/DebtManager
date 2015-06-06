package client.windows;

import client.controllers.BudgetController;
import client.controllers.SettleController;
import common.Budget;
import common.Payment;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SettleWindow extends Stage {
   public SettleWindow(Budget budget, ObservableList<Payment> paymentsToSettle,
                       BudgetController parentController) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SettleWindow.fxml"));
      Parent root = fxmlLoader.load();
      SettleController controller = fxmlLoader.<SettleController>getController();

      controller.setStage(this);
      controller.setBudget(budget, paymentsToSettle, parentController);
      controller.fillAllTables();

      setTitle("DeptManager - settle " + budget.getName());
      setScene(new Scene(root));
      setOnCloseRequest(event -> controller.clearTable());
      initModality(Modality.WINDOW_MODAL);
   }
}
