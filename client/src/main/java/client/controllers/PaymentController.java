package client.controllers;

import common.data.Budget;
import common.data.Payment;
import common.data.User;
import client.utils.InputFormatRestrictions;
import client.view.Alerts;
import client.view.ErrorHighlighter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class PaymentController extends BasicController implements Initializable {

  @FXML
  public Label labelWindowTitle, labelError;
  @FXML
  protected Button buttonLeft, buttonSave;
  @FXML
  protected ComboBox<User> boxChoosePayer;
  @FXML
  protected TextField fieldAmount;
  @FXML
  protected TextArea fieldDescription;

  private static final String AMOUNT_REGEX = "\\d{0,12}(\\.\\d{0,2})?";
  protected Budget budget;

  public void setBudget(Budget budget) {
    this.budget = budget;
  }

  public void initParticipantsList(ObservableList<User> participants) {
    boxChoosePayer.setEditable(false);
    boxChoosePayer.setItems(participants);
    boxChoosePayer.setValue(getPaymentOwner(participants));
    if (!isCurrentUserBudgetOwner())
      boxChoosePayer.setDisable(true);
  }

  protected abstract User getPaymentOwner(List<User> participants);

  private boolean isCurrentUserBudgetOwner() {
    return Objects.equals(currentUser, budget.getOwner());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initButtons();
    initBoxChoosePayer();
    initFields();
  }

  private void initFields() {
    fieldDescription.textProperty()
        .addListener(
            InputFormatRestrictions.restrictTextLength(fieldDescription::setText, Payment.MAX_DESCRIPTION_LENGTH));
    fieldAmount.textProperty()
        .addListener(InputFormatRestrictions.restrictTextFormat(fieldAmount::setText, AMOUNT_REGEX));
  }

  protected void initButtons() {
    buttonSave.setOnAction(event -> savePayment());
  }

  private void initBoxChoosePayer() {
    Callback<ListView<User>, ListCell<User>> cellFactory = param -> new ListCell<User>() {
      @Override
      protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (user != null)
          setText(user.getName());
      }
    };
    boxChoosePayer.setButtonCell(cellFactory.call(null));
    boxChoosePayer.setCellFactory(cellFactory);
  }

  protected void savePayment() {
    clearErrorHighlights();
    final User chosenUser = boxChoosePayer.getSelectionModel().getSelectedItem();
    final BigDecimal amount = new BigDecimal(fieldAmount.getText());
    try {
      savePaymentInDatabase(chosenUser, amount);
      currentStage.close();
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  protected abstract void savePaymentInDatabase(User user, BigDecimal amount) throws RemoteException;

  @Override
  protected void clearErrorHighlights() {
    labelError.setText("");
    ErrorHighlighter.unhighlightFields(fieldAmount, fieldDescription);
  }
}
