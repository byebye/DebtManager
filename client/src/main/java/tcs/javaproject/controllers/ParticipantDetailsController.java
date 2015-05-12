package tcs.javaproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tcs.javaproject.User;
import tcs.javaproject.windows.LoginWindow;

import java.net.URL;
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

   private User participant;
   private int budgetId;
   private final DatabaseController dbController = LoginWindow.dbController;

   public void setParticipant(User participant) {
      this.participant = participant;
      nameField.setText(participant.getName());
      emailField.setText(participant.getEmail());
      bankAccountField.setText(participant.getBankAccount());
   }

   public void setBudgetId(int budgetId) {
      this.budgetId = budgetId;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      btnCloseWindow.setOnAction(event -> {
         Stage stage = (Stage) btnCloseWindow.getScene().getWindow();
         stage.close();
      });
      btnRemoveParticipant.setOnAction(event -> {
         dbController.removeParticipant(budgetId, participant.getId());
         Stage stage = (Stage) btnRemoveParticipant.getScene().getWindow();
         stage.close();
      });
   }
}
