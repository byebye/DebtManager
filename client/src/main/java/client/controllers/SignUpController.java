package client.controllers;

import common.data.BankAccount;
import common.data.Email;
import common.data.User;
import common.utils.SHA1Hasher;
import client.utils.InputFormatRestrictions;
import client.view.Alerts;
import client.view.ErrorHighlighter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUpController extends BasicController implements Initializable {

  @FXML
  private Label labelError;
  @FXML
  private TextField fieldEmail, fieldUsername, fieldBankAccount;
  @FXML
  private PasswordField fieldPassword, fieldRepeatedPassword;
  @FXML
  private Button buttonSignUp, buttonCancel;

  private LoginController loginController;

  public void setLoginController(LoginController controller) {
    loginController = controller;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initButtons();
    initFields();
  }

  private void initFields() {
    fieldEmail.textProperty()
        .addListener(InputFormatRestrictions.restrictTextLength(fieldEmail::setText, Email.MAX_LENGTH));
    fieldUsername.textProperty()
        .addListener(InputFormatRestrictions.restrictTextLength(fieldUsername::setText, User.MAX_NAME_LENGTH));
    fieldBankAccount.textProperty()
        .addListener(InputFormatRestrictions.restrictBankAccountInput(fieldBankAccount::setText));
  }

  private void initButtons() {
    buttonCancel.setOnAction(event -> currentStage.close());
    buttonSignUp.setOnAction(event -> signUp());
  }

  private void signUp() {
    if (validateNewUserData() && createNewUser()) {
      Alerts.userCreated();
      loginController.fillDataFields(fieldEmail.getText(), fieldPassword.getText());
      currentStage.close();
    }
  }

  private boolean validateNewUserData() {
    clearErrorHighlights();
    if (!Email.isValid(fieldEmail.getText())) {
      labelError.setText("Invalid email address");
      ErrorHighlighter.highlightInvalidFields(fieldEmail);
    }
    else if (fieldUsername.getText().isEmpty()) {
      labelError.setText("User name cannot be empty");
      ErrorHighlighter.highlightInvalidFields(fieldUsername);
    }
    else if (!BankAccount.isValid(fieldBankAccount.getText())) {
      labelError.setText("Invalid bank account number");
      ErrorHighlighter.highlightInvalidFields(fieldBankAccount);
    }
    else if (!Objects.equals(fieldPassword.getText(), fieldRepeatedPassword.getText())) {
      labelError.setText("Passwords don't match");
      ErrorHighlighter.highlightInvalidFields(fieldRepeatedPassword, fieldPassword);
    }
    else if (fieldPassword.getText().isEmpty()) {
      labelError.setText("Password cannot be empty");
      ErrorHighlighter.highlightInvalidFields(fieldPassword);
    }
    else {
      return true;
    }
    return false;
  }

  private boolean createNewUser() {
    final Email email = new Email(fieldEmail.getText());
    final String userName = fieldUsername.getText();
    final BankAccount bankAccount = new BankAccount(fieldBankAccount.getText());
    final String passwordHash = SHA1Hasher.hash(fieldPassword.getText());
    try {
      accessProvider.signUp(email, userName, passwordHash, bankAccount);
    }
    catch (Exception e) {
      e.printStackTrace();
      // TODO display more informative message, e.g. "User already exists"
      labelError.setText("User couldn't be created! Try again");
      return false;
    }
    return true;
  }

  @Override
  protected void clearErrorHighlights() {
    labelError.setText("");
    ErrorHighlighter.unhighlightFields(
        fieldEmail,
        fieldUsername,
        fieldBankAccount,
        fieldPassword,
        fieldRepeatedPassword);
  }
}
