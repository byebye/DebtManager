package client.controllers;

import client.windows.BudgetWindow;
import common.DBHandler;
import common.User;
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

import java.net.URL;
import java.rmi.RemoteException;
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

   private static DBHandler dbController = LoginController.dbController;

   private final ObservableList<User> participantsList = FXCollections.observableArrayList();
   private BudgetWindow parent;

   public void setParent(BudgetWindow parent) {
      this.parent = parent;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      colParticipantName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
      colParticipantEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
      tabParticipants.setItems(participantsList);

      btnAddUser.setOnAction(event -> {
         User user = null;
         try {
            user = dbController.getUserByEmail(txtFieldEnterEmail.getText());
         }
         catch (RemoteException e) {
            e.printStackTrace();
         }
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
