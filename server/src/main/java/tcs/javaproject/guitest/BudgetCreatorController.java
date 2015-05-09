
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
import org.jooq.*;
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

   @Override
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
         User p = existsUser(txtFieldEnterMail.getText());
         if(p != null) {
            participantsList.add(p);
            txtUsersAdded.setText(txtUsersAdded.getText() + "\n" + txtFieldEnterMail.getText());
         }
         else{
            txtFieldEnterMail.setText("User not found!");
         }
      });
   }

   private User existsUser(String mail){
      String url = "jdbc:postgresql://localhost/debtmanager";

      try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
         Result result = create.selectFrom(Users.USERS)
                 .where(Users.USERS.EMAIL.equal(mail)).fetch();

         if(result.isEmpty())
            return null;

         return new User((int)result.intoArray(Users.USERS.ID)[0],(String)result.intoArray(Users.USERS.NAME)[0],mail);
      }
      catch (Exception e) {
         e.printStackTrace();
         return null;
      }
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

         create.insertInto(UserBudget.USER_BUDGET,
               UserBudget.USER_BUDGET.BUDGET_ID,
               UserBudget.USER_BUDGET.USER_ID)
               .values(budgetId, userId)
               .execute();
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
}

