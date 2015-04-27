package tcs.javaproject.guitest;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Users;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;

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

      TextArea dbQueryResult = new TextArea();
      grid.add(dbQueryResult, 0, 8, 8, 6);

      primaryStage.setScene(new Scene(grid, 350, 500));
      primaryStage.show();

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
            if (createUser(dbQueryResult, emailValue, usernameValue, bankAccountValue, passwordValue)) {
               actiontarget.setFill(Color.GREEN);
               actiontarget.setText("User created successfully!");
            }
            else {
               actiontarget.setText("User couldn't be created!");
            }
         }
      });
   }

   private boolean createUser(TextArea dbQueryResult, String email, String name, BigInteger bankAccount, String passwordHash) {
      String url = "jdbc:postgresql://localhost/debtmanager";

      try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
         create.insertInto(Users.USERS,
                 Users.USERS.EMAIL,
                 Users.USERS.NAME,
                 Users.USERS.BANK_ACCOUNT,
                 Users.USERS.PASSWORD_HASH)
                 .values(email, name, bankAccount, passwordHash).execute();

         Result<Record> result = create.select().from(Users.USERS).fetch();
         dbQueryResult.clear();

         for (Record r : result) {
            Integer id = r.getValue(Users.USERS.ID);
            String emailV = r.getValue(Users.USERS.EMAIL);
            String nameV = r.getValue(Users.USERS.NAME);
            BigInteger account = r.getValue(Users.USERS.BANK_ACCOUNT);
            String row = "ID: " + id + " email: " + emailV + " name: " + nameV + " account: " + account;
            System.out.println(row);
            dbQueryResult.appendText(row+"\n");
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         return false;
      }
      return true;
   }

   public static void main(String[] args) {
      launch(args);
   }
}
