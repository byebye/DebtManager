package client.controllers;

import client.windows.BudgetsListWindow;
import client.windows.SignUpWindow;
import common.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

   @FXML
   Button btnSignUp,btnLogIn;
   @FXML
   TextField txtFieldEmail;
   @FXML
   PasswordField txtFieldPassword;

   public static DBHandler dbController;
   public static AccessProvider ac;
   public static User currentUser;

   private static final String host = "localhost"; //"michalglapa.student.tcs.uj.edu.pl";

   public void setDbController(DBHandler dbhandler){
      dbController = dbhandler;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {

      //RMI connection
      if(System.getSecurityManager() == null)
         System.setSecurityManager(new SecurityManager());

      try {
         ac = (AccessProvider) LocateRegistry.getRegistry(host).lookup("AccessProvider");
      }
      catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }

      //Buttons
      btnSignUp.setOnAction(event -> {
         try {
            SignUpWindow signUpWindow = new SignUpWindow();
            Stage loginWindow = (Stage) btnSignUp.getScene().getWindow();
            signUpWindow.initOwner(loginWindow);
            signUpWindow.showAndWait();
         } catch(Exception e){
            e.printStackTrace();
         }
      });

      btnLogIn.setOnAction(event -> {
         if(System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

         try {
            String passwordHash = SHA1Hasher.hash(txtFieldPassword.getText());

            dbController = (DBHandler) ac.getDBHandler(new Email(txtFieldEmail.getText()), passwordHash);
            currentUser = dbController.getUserByEmail(txtFieldEmail.getText());
            BudgetsListWindow budgetsListWindow = new BudgetsListWindow(txtFieldEmail.getText());
            Stage loginWindow = (Stage) btnLogIn.getScene().getWindow();
            budgetsListWindow.setOnHidden(e -> loginWindow.show());
            loginWindow.hide();
            budgetsListWindow.showAndWait();
            txtFieldEmail.clear();
            txtFieldPassword.clear();
         }
         catch(Exception e){
            e.printStackTrace();
         }
      });
   }
}
