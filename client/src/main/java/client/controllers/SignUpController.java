package client.controllers;

import client.view.ErrorHighlighter;
import common.Email;
import common.SHA1Hasher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
      TextFormatter<String> onlyDigitsFormatter = new TextFormatter<>(change -> {
         change.setText(change.getText().replaceAll("[^0-9 ]", ""));
         return change;
      });
      txtFieldBankAccount.setTextFormatter(onlyDigitsFormatter);

      //Buttons
      btnCancel.setOnAction(event -> currentStage.close());

      btnSignUp.setOnAction(event -> {
         errorLabel.setText("");
         ErrorHighlighter.unhighlitghtFields(txtFieldEmail, txtFieldUsername, txtFieldBankAccount, txtFieldPassword,
                                             txtFieldRepPassword);
         if (!Email.isValid(txtFieldEmail.getText())) {
            errorLabel.setText("Invalid email address");
            ErrorHighlighter.highlightInvalidFields(txtFieldEmail);
         }
         else if (txtFieldUsername.getText().isEmpty()) {
            errorLabel.setText("User name should not be empty");
            ErrorHighlighter.highlightInvalidFields(txtFieldUsername);
         }
         else if (!txtFieldBankAccount.getText().replaceAll("\\s", "").matches("\\d{22}")) {
            errorLabel.setText("Invalid bank account number");
            ErrorHighlighter.highlightInvalidFields(txtFieldBankAccount);
            event.consume();
         }
         else if (!txtFieldPassword.getText().equals(txtFieldRepPassword.getText())) {
            errorLabel.setText("Passwords don't match");
            ErrorHighlighter.highlightInvalidFields(txtFieldPassword, txtFieldRepPassword);
            event.consume();
         }
         else if (txtFieldPassword.getText().isEmpty()) {
            errorLabel.setText("Password should not be empty");
            ErrorHighlighter.highlightInvalidFields(txtFieldPassword);
         }
         else {
            try {
               tryToSignUp();
               displayUserCreatedAlert();
               loginController.fillDataFields(txtFieldEmail.getText(), txtFieldPassword.getText());
            }
            catch (Exception e) {
               e.printStackTrace();
               errorLabel.setText("User couldn't be created! Try again");
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
