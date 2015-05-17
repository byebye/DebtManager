package client.windows;

import client.controllers.SignUpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpWindow extends Stage {

   public SignUpWindow() throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SignUpWindow.fxml"));
      Parent root = fxmlLoader.load();
      SignUpController controller = fxmlLoader.<SignUpController>getController();
      setTitle("DeptManager - sign up");
      setScene(new Scene(root));
   }
}