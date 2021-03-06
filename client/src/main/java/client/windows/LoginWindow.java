package client.windows;

import client.UpdateLongpollingCallbackRegistrar;
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

    final String host = getParameters().getNamed().get("host");

    UpdateLongpollingCallbackRegistrar.setHost(host);
    UpdateLongpollingCallbackRegistrar.start();

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
    Parent root = fxmlLoader.load();
    Scene scene = new Scene(root);
    controller = fxmlLoader.getController();

    controller.connectWithRMIHost(host);
    controller.setCurrentStage(primaryStage);

    primaryStage.setTitle("DebtManager - Log in");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    try {
      launch(args);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
