package tcs.javaproject.guitest;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginWindow extends Application {

   @Override
   public void start(Stage primaryStage) throws Exception {
      primaryStage.setTitle("DebtManager - Log In");

      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(25, 25, 25, 25));

      grid.add(new Text("Please log in to view your budgets"), 0, 0, 2, 1);

      TextField username = new TextField();
      username.setPromptText("Username");
      PasswordField password = new PasswordField();
      password.setPromptText("Password");

      grid.add(new Label("Username:"), 0, 1);
      grid.add(username, 1, 1, 2, 1);
      grid.add(new Label("Password:"), 0, 2);
      grid.add(password, 1, 2, 2, 1);

      Button signUpButton = new Button("Sign up");
      signUpButton.setOnAction(event -> {
         SignUpWindow signUpWindow = new SignUpWindow();
         signUpWindow.show();
      });
      HBox leftButtonBox = new HBox(signUpButton);
      leftButtonBox.setAlignment(Pos.CENTER_LEFT);

      Button logInButton = new Button("Log In");
      HBox rightButtonBox = new HBox(logInButton);
      rightButtonBox.setAlignment(Pos.CENTER_RIGHT);
      grid.add(leftButtonBox, 0, 3);
      grid.add(rightButtonBox, 2, 3);

      primaryStage.setScene(new Scene(grid, 300, 275));
      primaryStage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }

}
