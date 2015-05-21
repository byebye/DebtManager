package client.controllers;

import client.windows.BudgetsListWindow;
import common.AccessProvider;
import common.DBHandler;
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

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
   @FXML
   TextField txtFieldEmail, txtFieldUsername, txtFieldBankAccount;
   @FXML
   PasswordField txtFieldPassword, txtFieldRepPassword;
   @FXML
   Button btnSignUp, btnCancel;
   @FXML
   Text txtResult;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnSignUp.setDisable(true);
      txtFieldEmail.textProperty().addListener((observable, oldValue, newValue) -> {
         btnSignUp.setDisable(newValue.trim().isEmpty());
      });
      btnSignUp.setOnAction(event -> {
         if (!txtFieldPassword.getText().equals(txtFieldRepPassword.getText())) {
            txtResult.setFill(Color.FIREBRICK);
            txtResult.setText("Passwords don't match");
            event.consume();
         } else if (!txtFieldBankAccount.getText().replaceAll("\\s", "").matches("\\d{22}")) {
            txtResult.setFill(Color.FIREBRICK);
            txtResult.setText("Bank account should contain 22 digits");
            event.consume();
         } else {
            String emailValue = txtFieldEmail.getText();
            String usernameValue = txtFieldUsername.getText();
            BigInteger bankAccountValue = new BigInteger(txtFieldBankAccount.getText().replaceAll("\\s", ""));
            String passwordValue = txtFieldPassword.getText();
            try{
               AccessProvider ac = LoginController.ac;
               ac.signUp(new Email(emailValue), usernameValue, bankAccountValue, SHA1Hasher.hash(passwordValue));
               LoginController.dbController = (DBHandler) ac.getDBHandler(new Email(emailValue), SHA1Hasher.hash(passwordValue));
               Alert userCreatedAlert = new Alert(Alert.AlertType.INFORMATION);
               userCreatedAlert.setTitle("Success");
               userCreatedAlert.setHeaderText("User created successfully!");
               userCreatedAlert.setContentText("You will be automatically logged in.");
               userCreatedAlert.setOnHidden(hiddenEvent -> {
                  try {
                     BudgetsListWindow budgetsListWindow = new BudgetsListWindow(emailValue);
                     budgetsListWindow.show();
                     Stage stage = (Stage) btnCancel.getScene().getWindow();
                     stage.close();
                  }catch (Exception e){
                     e.printStackTrace();
                  }
               });
               userCreatedAlert.showAndWait();

            } catch(Exception e){
               e.printStackTrace();
               txtResult.setText("User couldn't be created!");
            }
         }
      });

      btnCancel.setOnAction(event -> {
         Stage stage = (Stage) btnCancel.getScene().getWindow();
         stage.close();
      });

   }
}
