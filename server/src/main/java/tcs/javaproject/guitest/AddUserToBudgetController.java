package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tcs.javaproject.database.DatabaseController;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUserToBudgetController implements Initializable {

   @FXML
   private TextField txtFieldEnterEmail;
   @FXML
   private Button btnAddUser;
   @FXML
   private Button btnConfirm;
   @FXML
   private TableView tabParticipants;
   @FXML
   public TableColumn colParticipantName;
   @FXML
   public TableColumn colParticipantEmail;

   private final DatabaseController dbController = LoginWindow.dbController;

   private int budgetId;
   private ObservableList<User> participantsList = FXCollections.observableArrayList();
   private BudgetWindow parent;

   public void setData(BudgetWindow parent) {
      this.parent = parent;
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
         else if (participantsList.contains(user)) {
            txtFieldEnterEmail.setText("User already added");
         }
         else {
            participantsList.add(user);
            txtFieldEnterEmail.clear();
         }
      });

      btnConfirm.setOnAction(event -> {
         parent.getController().addParticipants(participantsList);
         Stage stage = (Stage) btnConfirm.getScene().getWindow();
         stage.close();
      });
   }
}
