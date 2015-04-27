package tcs.javaproject.guitest;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by byebye on 27.04.15.
 */
public class SignUpWindow extends Application {

   @Override
   public void start(Stage primaryStage) throws Exception {
      primaryStage.setTitle("DebtManager - Sign Up");

      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(25, 25, 25, 25));

      grid.add(new Text("Create new account"), 0, 0, 2, 1);

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
      grid.add(email, 1, 1, 2, 1);
      grid.add(new Label("Username:"), 0, 2);
      grid.add(username, 1, 2, 2, 1);
      grid.add(new Label("Bank account:"), 0, 3);
      grid.add(bankAccount, 1, 3, 2, 1);
      grid.add(new Label("Password:"), 0, 4);
      grid.add(password, 1, 4, 2, 1);
      grid.add(new Label("Repeat password:"), 0, 5);
      grid.add(passwordRepeated, 1, 5, 2, 1);

      Button cancelButton = new Button("Cancel");
      HBox leftButtonBox = new HBox(cancelButton);
      leftButtonBox.setAlignment(Pos.CENTER_LEFT);

      Button signUpButton = new Button("Sign Up");
      HBox rightButtonBox = new HBox(signUpButton);
      rightButtonBox.setAlignment(Pos.CENTER_RIGHT);
      grid.add(leftButtonBox, 0, 6);
      grid.add(rightButtonBox, 2, 6);

      primaryStage.setScene(new Scene(grid, 300, 300));
      primaryStage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
