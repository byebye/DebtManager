package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.UserBudget;
import tcs.javaproject.database.tables.Users;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Vsmasster on 09.05.15.
 */
public class AddUserController implements Initializable{

   @FXML
   TextField txtFieldMail;
   @FXML
   Button btnAdd;
   @FXML
   Button btnConfirm;
   @FXML
   Text txtUsersAdded;

   private int budgetId;
   private List<User> usersList = new ArrayList<User>();
   private BudgetWindow parent;
   public void setData(int budgetId, BudgetWindow parent){this.budgetId = budgetId;this.parent = parent;}

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      btnAdd.setOnAction(event->{
         User user = userExist(txtFieldMail.getText());
         if(user != null){
            txtUsersAdded.setText(txtUsersAdded.getText()+"\n"+user.getName());
            usersList.add(user);
         }
         else txtFieldMail.setText("User not found!");
      });

      btnConfirm.setOnAction(event -> {
         addUsers();
         parent.getController().fillTabParticipants();
         Stage stage = (Stage)btnConfirm.getScene().getWindow();
         stage.close();
      });
   }

   private void addUsers(){
      String url = "jdbc:postgresql://localhost/debtmanager";

      try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
         for (User user : usersList) {
            create.insertInto(UserBudget.USER_BUDGET,
                  UserBudget.USER_BUDGET.BUDGET_ID,
                  UserBudget.USER_BUDGET.USER_ID)
                  .values(budgetId, user.getId())
                  .execute();
         }
      } catch (Exception e) {
            e.printStackTrace();
      }
   }

   private User userExist(String mail){
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
}
