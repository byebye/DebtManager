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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jooq.*;
import org.jooq.impl.DSL;
import tcs.javaproject.database.DatabaseController;
import tcs.javaproject.database.tables.Users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class LoginWindow extends Application {

   public static DatabaseController dbController = new DatabaseController();

   @Override
   public void start(Stage primaryStage) throws Exception {
      primaryStage.setTitle("DebtManager - Log In");

      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(25, 25, 25, 25));

      grid.add(new Text("Please log in to view your budgets"), 0, 0, 2, 1);

      TextField email = new TextField();
      email.setPromptText("Email");
      PasswordField password = new PasswordField();
      password.setPromptText("Password");

      grid.add(new Label("Email:"), 0, 1);
      grid.add(email, 1, 1, 2, 1);
      grid.add(new Label("Password:"), 0, 2);
      grid.add(password, 1, 2, 2, 1);

      final Text actionInfo = new Text();
      grid.add(actionInfo, 0, 4, 4, 1);

      Button signUpButton = new Button("Sign up");
      signUpButton.setOnAction(event -> {
         SignUpWindow signUpWindow = new SignUpWindow();
         signUpWindow.show();
      });
      HBox leftButtonBox = new HBox(signUpButton);
      leftButtonBox.setAlignment(Pos.CENTER_LEFT);

      Button logInButton = new Button("Log In");
      logInButton.setOnAction(event -> {
         if (dbController.validateUserPassword(email.getText(), password.getText())) {
            try {
               BudgetsListWindow budgetsListWindow = new BudgetsListWindow(email.getText());
               budgetsListWindow.show();
            }
            catch (IOException e) {
               e.printStackTrace();
            }
            email.clear();
            password.clear();
            actionInfo.setText("");
         }
         else {
            actionInfo.setFill(Color.FIREBRICK);
            actionInfo.setText("Incorrect email or password!");
         }
      });
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
