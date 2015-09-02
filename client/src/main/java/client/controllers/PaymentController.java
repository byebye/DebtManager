package client.controllers;

import client.utils.DataFormatListeners;
import client.view.Alerts;
import client.view.ErrorHighlighter;
import common.data.Budget;
import common.data.User;
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
  public Label labelWindowTitle;
  @FXML
  protected Button buttonLeft, buttonSave;
  @FXML
  protected ComboBox<User> boxChooseWho;
  @FXML
  protected TextField fieldAmount;
  @FXML
  protected TextArea fieldWhat;
  @FXML
  protected Label labelError;

  private static final String AMOUNT_REGEX = "\\d*(\\.\\d{0,2})?";
  protected Budget budget;

  public void setBudget(Budget budget) {
    this.budget = budget;
  }

  public void initParticipantsList(ObservableList<User> participants) {
    boxChooseWho.setEditable(false);
    boxChooseWho.setItems(participants);
    boxChooseWho.setValue(getPaymentOwner(participants));
    if (!isCurrentUserBudgetOwner())
      boxChooseWho.setDisable(true);
  }

  protected abstract User getPaymentOwner(List<User> participants);

  private boolean isCurrentUserBudgetOwner() {
    return Objects.equals(currentUser, budget.getOwner());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initButtons();
    initBoxChoose();
    fieldAmount.textProperty()
        .addListener(DataFormatListeners.restrictTextFormat(fieldAmount::setText, AMOUNT_REGEX));
  }

  protected void initButtons() {
    buttonSave.setOnAction(event -> savePayment());
  }

  private void initBoxChoose() {
    Callback<ListView<User>, ListCell<User>> cellFactory = param -> new ListCell<User>() {
      @Override
      protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (user != null)
          setText(user.getName());
      }
    };
    boxChooseWho.setButtonCell(cellFactory.call(null));
    boxChooseWho.setCellFactory(cellFactory);
  }

  protected void savePayment() {
    clearErrorHighlights();
    final User chosenUser = boxChooseWho.getSelectionModel().getSelectedItem();
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
    ErrorHighlighter.unhighlightFields(fieldAmount, fieldWhat);
  }
}
