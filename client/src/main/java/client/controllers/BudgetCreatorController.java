
package client.controllers;

import client.view.ErrorHighlighter;
import common.Budget;
import common.DBHandler;
import common.User;
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

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
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
   private TableColumn colParticipantName, colParticipantEmail, colAction;
   @FXML
   private Label errorLabel;

   private static DBHandler dbController = LoginController.dbController;
   private final User owner = LoginController.currentUser;
   private final ObservableList<User> participantsList = FXCollections.observableArrayList();

   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      colParticipantName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
      colParticipantEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
      colAction.setCellFactory(param -> new RemoveParticipantCell());
      tabParticipants.setItems(participantsList);

      btnAddUser.setOnAction(event -> {
         errorLabel.setText("");
         ErrorHighlighter.unhighlitghtFields(txtFieldEnterEmail);
         User user = null;
         try {
            user = dbController.getUserByEmail(txtFieldEnterEmail.getText());
            if (user == null) {
               errorLabel.setText("User not found!");
               ErrorHighlighter.highlightInvalidFields(txtFieldEnterEmail);
            }
            else if (participantsList.contains(user)) {
               errorLabel.setText("User already added");
               ErrorHighlighter.highlightInvalidFields(txtFieldEnterEmail);
            }
            else
               participantsList.add(user);
         }
         catch (RemoteException e) {
            errorLabel.setText("Server connection error");
            e.printStackTrace();
         }
      });

      btnCreateBudget.setOnAction(event -> {
         errorLabel.setText("");
         ErrorHighlighter.unhighlitghtFields(txtFieldEnterEmail);
         try {
            List<User> participantsSerializable = new ArrayList<User>(participantsList);
            Budget budget = new Budget(owner, budgetName.getText(), budgetDescription.getText(), participantsSerializable);
            if (dbController.createBudget(budget))
               currentStage.close();
            else
               errorLabel.setText("Budget couldn't be created! Try again");
         }
         catch (RemoteException e) {
            e.printStackTrace();
         }
      });

      btnCancel.setOnAction(event -> currentStage.close());
   }

   private class RemoveParticipantCell extends TableCell<User, Boolean> {
      final Button btnRemove = new Button();
      {
         btnRemove.setPrefSize(25, 25);
         btnRemove.setPadding(new Insets(5, 5, 5, 5));
      }

      public RemoveParticipantCell() {
         setPadding(new Insets(0, 0, 0, 0));
         btnRemove.setGraphic(loadRemoveImage());
         if(getTableRow().getIndex() == 0)
            btnRemove.setDisable(true);
         else {
            btnRemove.setOnAction(event -> {
               User participant = (User) RemoveParticipantCell.this.getTableRow().getItem();
               participantsList.remove(participant);
            });
         }
      }

      public ImageView loadRemoveImage() {
         Image removeImage = new Image(getClass().getResourceAsStream("/graphics/remove_button.png"));
         ImageView removeImageView = new ImageView(removeImage);
         removeImageView.setPreserveRatio(true);
         removeImageView.setFitHeight(12);
         return removeImageView;
      }

      @Override
      protected void updateItem(Boolean item, boolean empty) {
         super.updateItem(item, empty);
         if (!empty)
            setGraphic(btnRemove);
         else
            setGraphic(null);
      }
   }
}

