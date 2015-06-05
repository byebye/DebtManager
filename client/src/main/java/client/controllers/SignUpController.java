package client.controllers;

import common.Email;
import common.SHA1Hasher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
   @FXML
   Label errorLabel;

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
         errorLabel.setText("");
         txtFieldEmail.setStyle("-fx-border-color: transparent;");
         txtFieldUsername.setStyle("-fx-border-color: transparent;");
         txtFieldBankAccount.setStyle("-fx-border-color: transparent;");
         txtFieldPassword.setStyle("-fx-border-color: transparent;");
         txtFieldRepPassword.setStyle("-fx-border-color: transparent;");
         if (!Email.isValid(txtFieldEmail.getText())) {
            errorLabel.setText("Invalid email address");
            txtFieldEmail.setStyle("-fx-border-color: #d60f0f;");
            txtFieldEmail.requestFocus();
         }
         else if(txtFieldUsername.getText().isEmpty()) {
            errorLabel.setText("User name should not be empty");
            txtFieldUsername.setStyle("-fx-border-color: #d60f0f;");
            txtFieldUsername.requestFocus();
         }
         else if (!txtFieldBankAccount.getText().replaceAll("\\s", "").matches("\\d{22}")) {
            txtFieldBankAccount.requestFocus();
            txtFieldBankAccount.setStyle("-fx-border-color: #d60f0f;");
            errorLabel.setText("Invalid bank account number");
            event.consume();
         }
         else if (!txtFieldPassword.getText().equals(txtFieldRepPassword.getText())) {
            errorLabel.setText("Passwords don't match");
            txtFieldPassword.requestFocus();
            txtFieldPassword.setStyle("-fx-border-color: #d60f0f;");
            txtFieldRepPassword.setStyle("-fx-border-color: #d60f0f;");
            event.consume();
         }
         else if(txtFieldPassword.getText().isEmpty()) {
            errorLabel.setText("Password should not be empty");
            txtFieldPassword.requestFocus();
            txtFieldPassword.setStyle("-fx-border-color: #d60f0f;");
         }
         else {
            try {
               tryToSignUp();
               displayUserCreatedAlert();
               loginController.fillDataFields(txtFieldEmail.getText(), txtFieldPassword.getText());
            }
            catch (Exception e) {
               e.printStackTrace();
               errorLabel.setText("User couldn't be created, try again");
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
