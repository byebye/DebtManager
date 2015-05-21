package client.windows;

import client.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginWindow extends Application {

   @Override
   public void start(Stage primaryStage) throws Exception {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
      Parent root = fxmlLoader.load();
      LoginController controller = fxmlLoader.<LoginController>getController();

      primaryStage.setTitle("DeptManager - Sign in");
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
