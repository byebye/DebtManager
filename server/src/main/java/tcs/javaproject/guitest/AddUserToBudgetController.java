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
import sun.rmi.runtime.Log;
import tcs.javaproject.database.DatabaseController;
import tcs.javaproject.database.tables.UserBudget;
import tcs.javaproject.database.tables.Users;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddUserToBudgetController implements Initializable {

   @FXML
   TextField txtFieldMail;
   @FXML
   Button btnAdd;
   @FXML
   Button btnConfirm;
   @FXML
   Text txtUsersAdded;

   private final DatabaseController dbController = LoginWindow.dbController;

   private int budgetId;
   private List<User> usersList = new ArrayList<User>();
   private BudgetWindow parent;

   public void setData(int budgetId, BudgetWindow parent) {
      this.budgetId = budgetId;
      this.parent = parent;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      btnAdd.setOnAction(event -> {
         User user = dbController.getUserByEmail(txtFieldMail.getText());
         if (user != null) {
            txtUsersAdded.setText(txtUsersAdded.getText() + "\n" + user.getName());
            usersList.add(user);
         }
         else txtFieldMail.setText("User not found!");
      });

      btnConfirm.setOnAction(event -> {
         dbController.addBudgetParticipants(budgetId, usersList);
         parent.getController().fillTabParticipants();
         Stage stage = (Stage) btnConfirm.getScene().getWindow();
         stage.close();
      });
   }
}
