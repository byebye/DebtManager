package client.controllers;

import client.windows.BudgetsListWindow;
import client.windows.SignUpWindow;
import common.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

   @FXML
   Button btnSignUp, btnLogIn;
   @FXML
   TextField txtFieldEmail;
   @FXML
   PasswordField txtFieldPassword;

   public static DBHandler dbController;
   public static AccessProvider ac;
   public static User currentUser;
   private static String host;

   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   public void setDbController(DBHandler dbhandler){
      dbController = dbhandler;
   }

   public void connectWithRMIHost(String host) {
      this.host = (host == null ? "localhost" : host);
      if(System.getSecurityManager() == null)
         System.setSecurityManager(new SecurityManager());
      try {
         ac = (AccessProvider) LocateRegistry.getRegistry(host).lookup("AccessProvider");
      }
      catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   }

   public void fillDataFields(String email, String password) {
      txtFieldEmail.setText(email);
      txtFieldPassword.setText(password);
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      btnSignUp.setOnAction(event -> {
         try {
            SignUpWindow signUpWindow = new SignUpWindow(this);
            signUpWindow.initOwner(currentStage);
            signUpWindow.showAndWait();
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      });

      btnLogIn.setOnAction(event -> {
         if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

         try {
            tryToLogIn();
            displayBudgetsListWindow();
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      });
   }

   private void tryToLogIn() throws RemoteException, AuthenticationException {
      final Email email = new Email(txtFieldEmail.getText());
      final String passwordHash = SHA1Hasher.hash(txtFieldPassword.getText());
      dbController = (DBHandler) ac.getDBHandler(email, passwordHash);
      currentUser = dbController.getUserByEmail(txtFieldEmail.getText());
   }

   private void displayBudgetsListWindow() throws IOException {
      BudgetsListWindow budgetsListWindow = new BudgetsListWindow();
      budgetsListWindow.setOnHidden(e -> currentStage.show());
      currentStage.hide();
      txtFieldPassword.clear();
      budgetsListWindow.showAndWait();
   }
}
