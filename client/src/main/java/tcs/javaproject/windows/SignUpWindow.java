package tcs.javaproject.windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tcs.javaproject.User;

import java.io.IOException;
import java.math.BigInteger;

public class SignUpWindow extends Stage {

   public SignUpWindow() {
      init();
   }

   private void init() {
      setTitle("DebtManager - Sign Up");

      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(25, 25, 25, 25));

      grid.add(new Text("Create new account"), 0, 0, 8, 1);

      TextField email = new TextField();
      email.setPromptText("Email");
      TextField username = new TextField();
      username.setPromptText("Username");
      TextField bankAccount = new TextField();
      bankAccount.setPromptText("Bank account");
      PasswordField password = new PasswordField();
      password.setPromptText("Password");
      PasswordField passwordRepeated = new PasswordField();
      passwordRepeated.setPromptText("Repeat password");

      grid.add(new Label("Email:"), 0, 1);
      grid.add(email, 1, 1, 6, 1);
      grid.add(new Label("Username:"), 0, 2);
      grid.add(username, 1, 2, 6, 1);
      grid.add(new Label("Bank account:"), 0, 3);
      grid.add(bankAccount, 1, 3, 6, 1);
      grid.add(new Label("Password:"), 0, 4);
      grid.add(password, 1, 4, 6, 1);
      grid.add(new Label("Repeat password:"), 0, 5);
      grid.add(passwordRepeated, 1, 5, 6, 1);
      
      final Text actiontarget = new Text();
      grid.add(actiontarget, 0, 6, 8, 1);

      Button cancelButton = new Button("Cancel");
      HBox leftButtonBox = new HBox(cancelButton);
      leftButtonBox.setAlignment(Pos.CENTER_LEFT);

      Button signUpButton = new Button("Sign Up");
      HBox rightButtonBox = new HBox(signUpButton);
      rightButtonBox.setAlignment(Pos.CENTER_RIGHT);

      grid.add(leftButtonBox, 0, 7);
      grid.add(rightButtonBox, 6, 7);

      setScene(new Scene(grid, 350, 500));

      cancelButton.setOnAction(event -> close());

      signUpButton.setDisable(true);
      email.textProperty().addListener((observable, oldValue, newValue) -> {
         signUpButton.setDisable(newValue.trim().isEmpty());
      });

      signUpButton.setOnAction(event -> {
         if (!password.getText().equals(passwordRepeated.getText())) {
            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText("Passwords don't match");
            event.consume();
         }
         else if (!bankAccount.getText().replaceAll("\\s", "").matches("\\d{22}")) {
            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText("Bank account should contain 22 digits");
            event.consume();
         }
         else {
            String emailValue = email.getText();
            String usernameValue = username.getText();
            BigInteger bankAccountValue = new BigInteger(bankAccount.getText().replaceAll("\\s", ""));
            String passwordValue = password.getText();
            User user = new User(-1, usernameValue, emailValue, bankAccountValue.toString());
            if (LoginWindow.dbController.createUser(user, passwordValue)) {
               Alert userCreatedAlert = new Alert(Alert.AlertType.INFORMATION);
               userCreatedAlert.setTitle("Success");
               userCreatedAlert.setHeaderText("User created successfully!");
               userCreatedAlert.setContentText("You will be automatically logged in.");
               userCreatedAlert.setOnHidden(hiddenEvent -> {
                  try {
                     BudgetsListWindow budgetsListWindow = new BudgetsListWindow(emailValue);
                     budgetsListWindow.show();
                     close();
                  }
                  catch (IOException e) {
                     e.printStackTrace();
                  }
               });
               userCreatedAlert.showAndWait();
            }
            else {
               actiontarget.setText("User couldn't be created!");
            }
         }
      });
   }
}
