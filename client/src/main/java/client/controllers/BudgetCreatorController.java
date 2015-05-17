
package client.controllers;

import client.windows.LoginWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import common.Budget;
import common.User;

import java.net.URL;
import java.util.ResourceBundle;

public class BudgetCreatorController implements Initializable {

   @FXML
   private TextField budgetName, txtFieldEnterEmail;
   @FXML
   private TextArea budgetDescription;
   @FXML
   private Button btnCreateBudget, btnAddUser, btnCancel;
   @FXML
   private TableView<User> tabParticipants;
   @FXML
   public TableColumn colParticipantName, colParticipantEmail, colAction;

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
      colAction.setCellFactory(param -> new RemoveParticipantCell());
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
         if (dbController.createBudget(budget))
            close();
         else {
            // Error
         }
      });

      btnCancel.setOnAction(event -> close());
   }

   private void close() {
      Stage stage = (Stage) btnCancel.getScene().getWindow();
      stage.close();
   }

   private class RemoveParticipantCell extends TableCell<User, Boolean> {
      final Button btnRemove = new Button();

      public RemoveParticipantCell() {
         setPadding(new Insets(0, 0, 0, 0));
         btnRemove.setPrefSize(25, 25);
         Image removeImage = new Image(getClass().getResourceAsStream("/graphics/remove_button.png"));
         ImageView removeImageView = new ImageView(removeImage);
         removeImageView.setPreserveRatio(true);
         removeImageView.setFitHeight(12);
         btnRemove.setPadding(new Insets(5, 5, 5, 5));
         btnRemove.setGraphic(removeImageView);
         btnRemove.setOnAction(event -> {
            User participant = (User) RemoveParticipantCell.this.getTableRow().getItem();
            participantsList.remove(participant);
         });
      }

      @Override
      protected void updateItem(Boolean item, boolean empty) {
         super.updateItem(item, empty);
         if (!empty)
            setGraphic(btnRemove);
      }
   }
}

