package client.windows;

import client.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginWindow extends Application {

   private LoginController controller;

   public LoginController getController() {
      return controller;
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
      Parent root = fxmlLoader.load();
      Scene scene = new Scene(root);
      controller = fxmlLoader.<LoginController>getController();

      final String host = getParameters().getNamed().get("host");
      controller.connectWithRMIHost(host);
      controller.setStage(primaryStage);

      primaryStage.setTitle("DebtManager - Log in");
      primaryStage.setScene(scene);
      primaryStage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
