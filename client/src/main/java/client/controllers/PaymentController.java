package client.controllers;

import common.data.Budget;
import common.data.Payment;
import common.data.User;
import client.utils.InputFormatRestrictions;
import client.view.Alerts;
import client.view.ErrorHighlighter;

import org.controlsfx.control.CheckComboBox;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public abstract class PaymentController extends BasicController implements Initializable {

  @FXML
  public GridPane rootPane;
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
  protected final CheckComboBox<User> boxOwingUsers = new CheckComboBox<>();

  private static final String AMOUNT_REGEX = "\\d{0,12}(\\.\\d{0,2})?";
  protected Budget budget;
  protected List<User> participants;

  public void setBudget(Budget budget) {
    this.budget = budget;
  }

  public void initParticipantsList(ObservableList<User> participants) {
    this.participants = participants;
    boxChoosePayer.setEditable(false);
    boxChoosePayer.setItems(participants);
    boxChoosePayer.setValue(getPaymentOwner(participants));
    if (!isCurrentUserBudgetOwner())
      boxChoosePayer.setDisable(true);

    boxOwingUsers.getItems().setAll(participants);
    checkOwingUsers();
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
    initBoxChooseOwingUsers();
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
    boxChoosePayer.setConverter(userToNameConverter());
    boxChoosePayer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      boxOwingUsers.getCheckModel().clearCheck(newValue);
      boxOwingUsers.getCheckModel().check(oldValue);
    });
  }

  private void initBoxChooseOwingUsers() {
    rootPane.add(boxOwingUsers, 1, 4);
    boxOwingUsers.setConverter(userToNameConverter());
  }

  protected abstract void checkOwingUsers();

  private StringConverter<User> userToNameConverter() {
    return new StringConverter<User>() {
      @Override
      public String toString(User user) {
        return user.getName();
      }

      @Override
      public User fromString(String userName) {
        return boxOwingUsers.getItems()
            .stream()
            .filter(user -> user.getName().equals(userName))
            .findFirst()
            .get();
      }
    };
  }

  private void savePayment() {
    clearErrorHighlights();
    if (fieldAmount.getText().isEmpty()) {
      labelError.setText("Amount must be specified");
      ErrorHighlighter.highlightInvalidFields(fieldAmount);
      return;
    }
    final User chosenUser = boxChoosePayer.getSelectionModel().getSelectedItem();
    final BigDecimal amount = new BigDecimal(fieldAmount.getText());
    boxOwingUsers.getCheckModel().clearCheck(chosenUser); // payer can't owe themselves
    final List<Integer> owingUserIds = mapUsersToIds(boxOwingUsers.getCheckModel().getCheckedItems());
    try {
      savePaymentInDatabase(chosenUser, amount, owingUserIds);
      currentStage.close();
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private List<Integer> mapUsersToIds(List<User> users) {
    return users.stream()
        .map(User::getId)
        .collect(Collectors.toList());
  }

  protected abstract void savePaymentInDatabase(User user, BigDecimal amount,
      Collection<Integer> owingUserIds) throws RemoteException;

  @Override
  protected void clearErrorHighlights() {
    labelError.setText("");
    ErrorHighlighter.unhighlightFields(fieldAmount, fieldDescription);
  }
}
