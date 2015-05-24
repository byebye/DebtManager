package client.controllers;

import common.Email;
import common.SHA1Hasher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.naming.AuthenticationException;
import java.math.BigInteger;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
   @FXML
   TextField txtFieldEmail, txtFieldUsername, txtFieldBankAccount;
   @FXML
   PasswordField txtFieldPassword, txtFieldRepPassword;
   @FXML
   Button btnSignUp, btnCancel;

   private Stage currentStage;
   private LoginController loginController;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   public void setLoginController(LoginController controller) {
      loginController = controller;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnCancel.setOnAction(event -> currentStage.close());

      btnSignUp.setOnAction(event -> {
         if (!txtFieldPassword.getText().equals(txtFieldRepPassword.getText())) {
//            txtResult.setFill(Color.FIREBRICK);
//            txtResult.setText("Passwords don't match");
            event.consume();
         }
         else if (!txtFieldBankAccount.getText().replaceAll("\\s", "").matches("\\d{22}")) {
//            txtResult.setFill(Color.FIREBRICK);
//            txtResult.setText("Bank account should contain 22 digits");
            event.consume();
         }
         else {
            try {
               tryToSignUp();
               displayUserCreatedAlert();
               loginController.fillDataFields(txtFieldEmail.getText(), txtFieldPassword.getText());
            }
            catch (Exception e) {
               e.printStackTrace();
//               txtResult.setText("User couldn't be created!");
            }
         }
      });
   }

   private void tryToSignUp() throws RemoteException, AuthenticationException {
      Email email = new Email(txtFieldEmail.getText());
      String userName = txtFieldUsername.getText();
      BigInteger bankAccount = new BigInteger(txtFieldBankAccount.getText().replaceAll("\\s", ""));
      String passwordHash = SHA1Hasher.hash(txtFieldPassword.getText());
      LoginController.ac.signUp(email, userName, bankAccount, passwordHash);
   }

   private void displayUserCreatedAlert() {
      Alert userCreatedAlert = new Alert(Alert.AlertType.INFORMATION);
      userCreatedAlert.setTitle("New account created");
      userCreatedAlert.setHeaderText("Account created successfully!");
      userCreatedAlert.setContentText("Now you can log in to view your budgets.");
      userCreatedAlert.showAndWait();
      currentStage.close();
   }
}
