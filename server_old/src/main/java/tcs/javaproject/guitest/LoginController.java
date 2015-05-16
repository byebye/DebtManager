package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import tcs.javaproject.database.DatabaseController;

import java.io.IOException;
import java.net.URL;
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

   private static DatabaseController dbController = LoginWindow.dbController;

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
         //System.out.println("DUPA");
         if (dbController.validateUserPassword(txtFieldEmail.getText(), txtFieldPassword.getText())) {
            try {
               BudgetsListWindow budgetsListWindow = new BudgetsListWindow(txtFieldEmail.getText());
               budgetsListWindow.show();
            } catch (IOException e) {
               e.printStackTrace();
            }
            txtFieldEmail.clear();
            txtFieldPassword.clear();
            txtReturnMessage.setText("");
         } else {
            txtReturnMessage.setFill(Color.FIREBRICK);
            txtReturnMessage.setText("Incorrect email or password!");
         }
      });
   }
}
