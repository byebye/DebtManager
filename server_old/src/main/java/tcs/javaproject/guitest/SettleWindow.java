package tcs.javaproject.guitest;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Vsmasster on 12.05.15.
 */
public class SettleWindow extends Stage {
   public SettleWindow(Budget budget,ObservableList<Payment> paymentsToSettle,BudgetController parentController) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SettleWindow.fxml"));
      Parent root = fxmlLoader.load();
      SettleController controller = fxmlLoader.<SettleController>getController();
      controller.setBudget(budget,paymentsToSettle,parentController);
      controller.fillAllTables();
      setTitle("DeptManager - settle " + budget.getName());
      setScene(new Scene(root));
   }
}
