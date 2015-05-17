package client.controllers;

import client.windows.BudgetsListWindow;
import client.windows.LoginWindow;
import client.windows.SignUpWindow;
import common.AccessProvider;
import common.DBHandler;
import common.Email;
import common.SHA1Hasher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.naming.AuthenticationException;
import java.io.IOException;
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
   @FXML
   Text txtReturnMessage;

   public static DBHandler dbController;

   public void setDbController(DBHandler dbhandler){
      dbController = dbhandler;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnSignUp.setOnAction(event -> {
         try {
            SignUpWindow signUpWindow = new SignUpWindow();
            signUpWindow.show();
         }catch(Exception e){
            e.printStackTrace();
         }
      });

      btnLogIn.setOnAction(event -> {
         if(System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

         try {
            AccessProvider ac = (AccessProvider) LocateRegistry.getRegistry("michalglapa.student.tcs.uj.edu.pl").lookup("AccessProvider");
            String passwordHash = SHA1Hasher.hash(txtFieldPassword.getText());

            dbController = (DBHandler) ac.getDBHandler(new Email(txtFieldEmail.getText()), passwordHash);

            BudgetsListWindow budgetsListWindow = new BudgetsListWindow(txtFieldEmail.getText());
            budgetsListWindow.show();
            txtFieldEmail.clear();
            txtFieldPassword.clear();
            txtReturnMessage.setText("");
         }
         catch(Exception e){
            txtReturnMessage.setText("Wrong login/password combination");
         }
      });
   }
}
