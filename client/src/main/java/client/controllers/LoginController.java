package client.controllers;

import common.connection.AccessProvider;
import common.connection.DbHandler;
import common.data.Email;
import common.utils.SHA1Hasher;
import client.view.Alerts;
import client.view.ErrorHighlighter;
import client.windows.BudgetsListWindow;
import client.windows.SignUpWindow;

import javax.naming.AuthenticationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ResourceBundle;

public class LoginController extends BasicController implements Initializable {

  @FXML
  private Label labelError;
  @FXML
  private TextField fieldEmail;
  @FXML
  private PasswordField fieldPassword;
  @FXML
  private Button buttonSignUp, buttonLogIn;

  private static String host;

  public void connectWithRMIHost(String host) {
    LoginController.host = (host == null ? "localhost" : host);
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new SecurityManager());
    try {
      accessProvider = (AccessProvider) LocateRegistry.getRegistry(host).lookup("AccessProvider");
    }
    catch (Exception e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
      System.exit(1);
    }
  }

  public void fillDataFields(String email, String password) {
    fieldEmail.setText(email);
    fieldPassword.setText(password);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttonSignUp.setOnAction(event -> displaySignUpWindow());
    buttonLogIn.setOnAction(event -> logIn());
    buttonLogIn.setOnKeyPressed(event -> {
      if (event.getCode().equals(KeyCode.ENTER))
        buttonLogIn.fire();
    });
  }

  private void displaySignUpWindow() {
    clearErrorHighlights();
    SignUpWindow signUpWindow = new SignUpWindow(this);
    signUpWindow.initOwner(currentStage);
    signUpWindow.showAndWait();
  }

  private void logIn() {
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new SecurityManager());
    if (authenticateUser())
      displayBudgetsListWindow();
  }

  private boolean authenticateUser() {
    clearErrorHighlights();
    final Email email = new Email(fieldEmail.getText());
    final String passwordHash = SHA1Hasher.hash(fieldPassword.getText());
    try {
      dbHandler = (DbHandler) accessProvider.getDbHandler(email, passwordHash);
      currentUser = dbHandler.getUserByEmail(fieldEmail.getText());
      return true;
    }
    catch (AuthenticationException | IllegalArgumentException e) {
      labelError.setText("Incorrect email or password");
      ErrorHighlighter.highlightInvalidFields(fieldPassword, fieldEmail);
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
    return false;
  }

  private void displayBudgetsListWindow() {
    BudgetsListWindow budgetsListWindow = new BudgetsListWindow();
    budgetsListWindow.setOnHidden(event -> currentStage.show());
    currentStage.hide();
    fieldPassword.clear();
    budgetsListWindow.show();
  }

  protected void clearErrorHighlights() {
    labelError.setText("");
    ErrorHighlighter.unhighlightFields(fieldEmail, fieldPassword);
  }
}
