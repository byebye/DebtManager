package client.windows;

import client.controllers.BankTransfersController;
import client.controllers.PaymentController;
import common.Budget;
import common.Payment;
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
public class BankTransfersWindow extends Stage {
   public BankTransfersWindow(int user) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BankTransfersWindow.fxml"));
      Parent root = fxmlLoader.load();
      BankTransfersController controller = fxmlLoader.<BankTransfersController>getController();
      controller.setUser(user);
      setTitle("DeptManager - Manage bank transfers");
      setScene(new Scene(root));
      initModality(Modality.WINDOW_MODAL);
   }
}
