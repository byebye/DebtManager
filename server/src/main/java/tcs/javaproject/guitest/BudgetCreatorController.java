
package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tcs.javaproject.database.DatabaseController;

import java.net.URL;
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

   private final DatabaseController dbController = LoginWindow.dbController;
   private int userId;
   private List<User> participantsList = new ArrayList<>();

   public void setUserId(int userId) {
      this.userId = userId;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {

      txtUsersAdded.setText("Participants list:");

      btnAddUser.setOnAction(event -> {
         User user = dbController.getUserByEmail(txtFieldEnterMail.getText());
         if (user != null) {
            participantsList.add(user);
            txtUsersAdded.setText(txtUsersAdded.getText() + "\n" + txtFieldEnterMail.getText());
         }
         else {
            txtFieldEnterMail.setText("User not found!");
         }
      });

      btnCreateBudget.setOnAction(event -> {
         Budget budget = new Budget(dbController.getUserById(userId), budgetName.getText(), budgetDescription.getText(), participantsList);
         if (dbController.createBudget(budget)) {
            Stage stage = (Stage) btnCreateBudget.getScene().getWindow();
            stage.close();
         }
         else {
            // Error
         }
      });
   }
}

