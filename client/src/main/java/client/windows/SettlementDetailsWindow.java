package client.windows;

import client.controllers.PaymentController;
import client.controllers.SettlementDetailsController;
import common.Budget;
import common.Payment;
import common.Settlement;
import common.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by vsmasster on 26.05.15.
 */
public class SettlementDetailsWindow extends Stage {
   public SettlementDetailsWindow(Settlement settlement,Budget budget) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SettlementDetailsWindow.fxml"));
      Parent root = fxmlLoader.load();
      SettlementDetailsController controller = fxmlLoader.<SettlementDetailsController>getController();
      controller.setData(settlement,budget);
      controller.fillContentList();
      setTitle("DeptManager - show settlement details");
      setScene(new Scene(root));
      initModality(Modality.WINDOW_MODAL);
   }
}
