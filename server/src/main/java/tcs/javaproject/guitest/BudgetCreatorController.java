
package tcs.javaproject.guitest;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Budgets;
import tcs.javaproject.database.tables.UserBudget;
import tcs.javaproject.database.tables.Users;
import tcs.javaproject.database.tables.records.BudgetsRecord;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetCreatorController implements Initializable {
   @FXML
   private TextField budgetName, txtFieldEnterMail;
   @FXML
   private TextArea budgetDescription;
   @FXML
   private Button btnCreateBudget, btnAddUser;
   @FXML
   private Text txtUsersAdded;

   private int userId;
   private List<User> participantsList = new ArrayList<>();
   public void setUserId(int userId) {
      this.userId = userId;
   }

   @Override //TODO finding user in database and adding items to participantsList
   public void initialize(URL location, ResourceBundle resources) {

      btnCreateBudget.setOnAction(event -> {
         if (createBudget()) {
            Stage stage = (Stage) btnCreateBudget.getScene().getWindow();
            stage.close();
         }
         else {
            // Error
         }
      });
      txtUsersAdded.setText("Users already added:");

      btnAddUser.setOnAction(event -> {
         if(existsUser(txtFieldEnterMail.getText())) {
            participantsList.add(new User(1,"userName",txtFieldEnterMail.getText()));
            txtUsersAdded.setText(txtUsersAdded.getText() + "\n" + txtFieldEnterMail.getText());
         }
         else{
            //displaying popup
         }
      });
   }

   private boolean existsUser(String mail){

      return false;
   }

   private boolean createBudget() {
      String url = "jdbc:postgresql://localhost/debtmanager";

      try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
         final Result<BudgetsRecord> result = create.insertInto(Budgets.BUDGETS,
                 Budgets.BUDGETS.NAME,
                 Budgets.BUDGETS.DESCRIPTION,
                 Budgets.BUDGETS.OWNER_ID)
                 .values(budgetName.getText(), budgetDescription.getText(), userId)
                 .returning(Budgets.BUDGETS.ID)
                 .fetch();
         int budgetId = result.get(0).value1();
         for (User user : participantsList) {
            create.insertInto(UserBudget.USER_BUDGET,
                    UserBudget.USER_BUDGET.BUDGET_ID,
                    UserBudget.USER_BUDGET.USER_ID)
                    .values(budgetId, user.getId())
                    .execute();
         }
         return true;
      }
      catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   public List<User> getUsersList() {
      String url = "jdbc:postgresql://localhost/debtmanager";

      try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
         Result<Record3<Integer, String, String>> result = create
                 .select(Users.USERS.ID, Users.USERS.NAME, Users.USERS.EMAIL)
                 .from(Users.USERS)
                 .fetch();
         List<User> users = new ArrayList<>();
         for (Record3<Integer, String, String> user : result) {
            int id = user.value1();
            String name = user.value2();
            String email = user.value3();
            users.add(new User(id, name, email));
         }
         return users;
      }
      catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public static class User {

      private int id;
      private String name;
      private String email;
      private BooleanProperty add;

      public User(int id, String name, String email) {
         this.id = id;
         this.name = name;
         this.email = email;
         this.add = new SimpleBooleanProperty(false);
      }

      public int getId() {
         return id;
      }

      public String getName() {
         return name;
      }

      public String getEmail() {
         return email;
      }

      public BooleanProperty getAdd() {
         return add;
      }

      public void updateAdd() {
         add.setValue(!add.getValue());
      }

      @Override
      public String toString() {
         return "User{" +
                 "id=" + id +
                 ", name='" + name + '\'' +
                 ", email='" + email + '\'' +
                 ", add='" + add + '\'' +
                 '}';
      }
   }
}

