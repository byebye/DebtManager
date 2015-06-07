package client.windows;

import client.SimpleCallback;
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

   public RemoteCallback rc;

   @Override
   public void start(Stage primaryStage) throws Exception {

      final String host = getParameters().getNamed().get("host");
      try {
         rc = new SimpleCallback();
         UnicastRemoteObject.exportObject(rc, 0);
         ((CallbackManager) LocateRegistry.getRegistry(host).lookup("UpdateManager")).register(rc);
         System.out.println("Callback registered");
      }
      catch (RemoteException |NotBoundException re) {
         System.out.println("Cannot register callback");
         re.printStackTrace();
      }

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
      try{launch(args);} catch (Exception e) {}
   }
}
