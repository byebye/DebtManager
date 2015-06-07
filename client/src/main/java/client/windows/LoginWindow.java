package client.windows;

import client.SimpleCallback;
import client.UpdateCallbackRegistrar;
import client.UpdateLongpollingCallbackRegistrar;
import client.controllers.LoginController;
import common.CallbackManager;
import common.RemoteCallback;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class LoginWindow extends Application {

   private LoginController controller;

   public LoginController getController() {
      return controller;
   }


   @Override
   public void start(Stage primaryStage) throws Exception {

      final String host = getParameters().getNamed().get("host");

      //UpdateCallbackRegistrar.setHost(host);
      //UpdateCallbackRegistrar.registerCallbackOnServer(new SimpleCallback());

      UpdateLongpollingCallbackRegistrar.setHost(host);
      UpdateLongpollingCallbackRegistrar.start();
      UpdateLongpollingCallbackRegistrar.registerCallbackOnServer(new SimpleCallback());

      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
      Parent root = fxmlLoader.load();
      Scene scene = new Scene(root);
      controller = fxmlLoader.<LoginController>getController();

      controller.connectWithRMIHost(host);
      controller.setStage(primaryStage);



      primaryStage.setTitle("DebtManager - Log in");
      primaryStage.setScene(scene);
      primaryStage.show();
   }

   public static void main(String[] args) {
      try{launch(args);} catch (Exception e) {e.printStackTrace();}
   }
}
