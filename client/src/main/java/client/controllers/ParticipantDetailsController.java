package client.controllers;

import common.data.Budget;
import common.data.User;
import client.view.Alerts;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ParticipantDetailsController extends BasicController implements Initializable {

  @FXML
  private TextField fieldName, fieldEmail, fieldBankAccount;
  @FXML
  private Button buttonRemoveParticipant, buttonClose;

  private User participant;
  private boolean hasUnaccountedPayments;
  private Budget budget;

  public void setBudget(Budget budget) {
    this.budget = budget;
    if (!isUserBudgetOwner(currentUser, budget))
      buttonRemoveParticipant.setDisable(true);
  }

  private boolean isUserBudgetOwner(User user, Budget budget) {
    return Objects.equals(user, budget.getOwner());
  }

  public void setParticipant(User participant, boolean hasUnaccountedPayments) {
    this.hasUnaccountedPayments = hasUnaccountedPayments;
    this.participant = participant;
    fillParticipantInfoFields(participant);
  }

  private void fillParticipantInfoFields(User participant) {
    fieldName.setText(participant.getName());
    fieldEmail.setText(participant.getEmail());
    fieldBankAccount.setText(participant.getBankAccount());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttonClose.setOnAction(event -> currentStage.close());
    buttonRemoveParticipant.setOnAction(event -> removeParticipant());
  }

  private void removeParticipant() {
    if (isUserBudgetOwner(participant, budget))
      Alerts.participantCannotBeRemoved("Owner of the budget cannot be removed.");
    if (hasUnaccountedPayments)
      Alerts.participantCannotBeRemoved("The participant must not have unaccounted payments.");
    else {
      try {
        dbHandler.removeParticipant(budget.getId(), participant.getId());
        currentStage.close();
      }
      catch (RemoteException e) {
        e.printStackTrace();
        Alerts.serverConnectionError();
      }
    }
  }

  @Override
  protected void clearErrorHighlights() {
  }
}
