package client.controllers;

import common.Budget;
import common.DBHandler;
import common.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ParticipantDetailsController implements Initializable {

   @FXML
   public TextField nameField;
   @FXML
   public TextField emailField;
   @FXML
   public TextField bankAccountField;
   @FXML
   public Button btnRemoveParticipant;
   @FXML
   public Button btnCloseWindow;

   private final User currentUser = LoginController.currentUser;
   private User participant;
   private boolean hasUnaccountedPayments;
   private Budget budget;
   private static DBHandler dbController = LoginController.dbController;

   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   public void setParticipant(User participant, boolean hasUnaccountedPayments) {
      this.hasUnaccountedPayments = hasUnaccountedPayments;
      this.participant = participant;
      nameField.setText(participant.getName());
      emailField.setText(participant.getEmail());
      bankAccountField.setText(participant.getBankAccount());
   }

   public void setBudget(Budget budget) {
      this.budget = budget;
      if (!currentUser.equals(budget.getOwner())) {
         btnRemoveParticipant.setDisable(true);
      }
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      btnCloseWindow.setOnAction(event -> currentStage.close());

      btnRemoveParticipant.setOnAction(event -> {
         if (hasUnaccountedPayments || participant.equals(budget.getOwner())) {
            Alert hasUnaccountedPaymentsAlert = new Alert(Alert.AlertType.ERROR);
            hasUnaccountedPaymentsAlert.setResizable(true);
            hasUnaccountedPaymentsAlert.setTitle("Participant cannot be removed");
            hasUnaccountedPaymentsAlert.setHeaderText("Participant cannot be removed!");
            if (participant.equals(budget.getOwner()))
               hasUnaccountedPaymentsAlert.setContentText("Owner of the budget cannot be removed.");
            else
               hasUnaccountedPaymentsAlert.setContentText("The participant must not have unaccounted payments.");
            hasUnaccountedPaymentsAlert.showAndWait();
         }
         else {
            try {
               dbController.removeParticipant(budget.getId(), participant.getId());
               currentStage.close();
            }
            catch (RemoteException e) {
               e.printStackTrace();
            }
         }
      });
   }
}
