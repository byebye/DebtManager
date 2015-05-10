
package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tcs.javaproject.database.DatabaseController;

import java.net.URL;
import java.util.ResourceBundle;

public class BudgetCreatorController implements Initializable {

   @FXML
   private TextField budgetName, txtFieldEnterEmail;
   @FXML
   private TextArea budgetDescription;
   @FXML
   private Button btnCreateBudget, btnAddUser;
   @FXML
   private TableView<User> tabParticipants;
   @FXML
   public TableColumn colParticipantName;
   @FXML
   public TableColumn colParticipantEmail;

   private final DatabaseController dbController = LoginWindow.dbController;
   private int userId;
   private final ObservableList<User> participantsList = FXCollections.observableArrayList();

   public void setUserId(int userId) {
      this.userId = userId;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      colParticipantName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
      colParticipantEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
      tabParticipants.setItems(participantsList);

      btnAddUser.setOnAction(event -> {
         User user = dbController.getUserByEmail(txtFieldEnterEmail.getText());
         if (user == null) {
            txtFieldEnterEmail.setText("User not found!");
         }
         else if (user.getId() == userId) {
            txtFieldEnterEmail.setText("You will be added automatically");
         }
         else if (participantsList.contains(user)) {
            txtFieldEnterEmail.setText("User already added");
         }
         else {
            participantsList.add(user);
            txtFieldEnterEmail.clear();
         }
      });

      btnCreateBudget.setOnAction(event -> {
         final User owner = dbController.getUserById(userId);
         participantsList.add(owner);
         Budget budget = new Budget(owner, budgetName.getText(), budgetDescription.getText(), participantsList);
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

