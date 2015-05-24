package client.windows;

import client.controllers.LoginController;
import client.controllers.SignUpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpWindow extends Stage {

   public SignUpWindow(LoginController loginController) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SignUpWindow.fxml"));
      Parent root = fxmlLoader.load();

      SignUpController controller = fxmlLoader.<SignUpController>getController();
      controller.setStage(this);
      controller.setLoginController(loginController);

      setTitle("DebtManager - Sign up");
      setScene(new Scene(root));
      initModality(Modality.APPLICATION_MODAL);
   }
}