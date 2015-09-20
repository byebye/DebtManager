package client.controllers;

import common.data.Budget;
import common.data.Payment;
import common.data.Settlement;
import common.data.User;
import client.BudgetExporter;
import client.view.Alerts;
import client.windows.AddParticipantsWindow;
import client.windows.AddPaymentWindow;
import client.windows.ParticipantDetailsWindow;
import client.windows.SettleWindow;
import client.windows.SettlementsHistoryWindow;
import client.windows.UpdatePaymentWindow;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BudgetController extends BasicController implements Initializable, SelfUpdating {

  @FXML
  private Label labelBudgetName, labelBudgetDescription;
  @FXML
  private Label labelSpentMoney, labelCurrentUserBalance;
  @FXML
  private Button buttonBudgetClose, buttonBudgetDelete, buttonBudgetExport;
  @FXML
  private Button buttonAddPayment, buttonAddParticipant, buttonSettle, buttonHistory, buttonBankTransfers;
  @FXML
  private ToggleButton buttonPaymentsPaid, buttonPaymentsOwed, buttonPaymentsOther;

  @FXML
  private TableView<Payment> tableUnsettledPayments;
  @FXML
  private TableColumn<Payment, String> columnUnsettledPayer, columnUnsettledDescription;
  @FXML
  private TableColumn<Payment, Integer> columnUnsettledAmount;
  @FXML
  private TableColumn<Payment, Boolean> columnConfirm;

  @FXML
  private TableView<User> tableParticipants;
  @FXML
  private TableColumn<User, String> columnUserName, columnUserMail;
  @FXML
  private TableColumn<User, BigDecimal> columnUserBalance;

  private final ObservableList<User> participantsList = FXCollections.observableArrayList();
  private final ObservableList<Payment> unsettledPayments = FXCollections.observableArrayList();
  private Budget budget;
  private double spentMoneySum = 0;

  public void setBudget(Budget budget) {
    this.budget = budget;
    labelBudgetName.setText(budget.getName());
    labelBudgetDescription.setText(budget.getDescription());
    if (!isCurrentUserBudgetOwner())
      disableOnlyBudgetOwnerButtons();
  }

  private void disableOnlyBudgetOwnerButtons() {
    buttonAddParticipant.setDisable(true);
    buttonSettle.setDisable(true);
    buttonBudgetDelete.setDisable(true);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initButtons();
    initTables();
  }

  private void initButtons() {
    buttonBudgetClose.setOnAction(event -> currentStage.close());
    buttonBudgetExport.setOnAction(event -> exportBudget());
    buttonBudgetDelete.setOnAction(event -> deleteBudget());

    buttonAddParticipant.setOnAction(event -> displayAddParticipantsWindow());
    buttonAddPayment.setOnAction(event -> displayAddPaymentWindow());
    buttonSettle.setOnAction(event -> settlePayments());
    buttonHistory.setOnAction(event -> displaySettlementsHistoryWindow());
  }

  private void exportBudget() {
    try {
      List<Payment> settledPayments = dbHandler.getAllPayments(budget.getId(), true);
      List<Settlement> settlements = dbHandler.getAllSettlementsOfBudget(budget.getId());
      BudgetExporter budgetExporter = new BudgetExporter(budget,
          participantsList,
          settledPayments,
          unsettledPayments,
          settlements,
          currentStage);
      budgetExporter.export();
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private void deleteBudget() {
    Optional<ButtonType> result = Alerts.deleteBudgetConfirmation();
    if (!result.isPresent() || result.get() != ButtonType.OK)
      return;
    try {
      dbHandler.deleteBudget(budget);
      currentStage.close();
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
    catch (Exception e) {
      e.printStackTrace();
      // TODO budget couldn't be deleted
    }
  }

  private void displayAddParticipantsWindow() {
    AddParticipantsWindow addParticipantsWindow = new AddParticipantsWindow(this);
    addParticipantsWindow.initOwner(currentStage);
    addParticipantsWindow.setOnHidden(event -> fillTableUnsettledPayments());
    addParticipantsWindow.show();
  }

  private void displayAddPaymentWindow() {
    AddPaymentWindow addPaymentWindow = new AddPaymentWindow(budget, participantsList);
    addPaymentWindow.initOwner(currentStage);
    addPaymentWindow.setOnHidden(event -> fillTableUnsettledPayments());
    addPaymentWindow.show();
  }

  private void settlePayments() {
    try {
      if (unsettledPayments.size() > 0)
        displaySettleWindow();
      else
        Alerts.noPaymentsToSettle();
    }
    catch (Exception e) {
      e.printStackTrace();
      // TODO display proper message
    }
  }

  private void displaySettleWindow() {
    final List<Payment> paymentsToSettle = getPaymentsToSettle();
    final Map<Integer, Double> usersBalance = participantsList.stream()
        .collect(Collectors.toMap(User::getId, user -> calculateUserBalance(user, paymentsToSettle)));
    SettleWindow settleWindow = new SettleWindow(this, budget, participantsList, paymentsToSettle, usersBalance);
    settleWindow.initOwner(currentStage);
    settleWindow.showAndWait();
  }

  private List<Payment> getPaymentsToSettle() {
    return unsettledPayments.stream()
        .filter(Payment::isAccepted)
        .collect(Collectors.toList());
  }

  private void displaySettlementsHistoryWindow() {
    SettlementsHistoryWindow historyWindow = new SettlementsHistoryWindow(budget);
    historyWindow.initOwner(currentStage);
    historyWindow.showAndWait();
  }

  private void initTables() {
    initUnsettledPaymentsTable();
    initParticipantsTable();
  }

  private void initUnsettledPaymentsTable() {
    columnUnsettledPayer.setCellValueFactory(new PropertyValueFactory<>("payer"));
    columnUnsettledDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    columnUnsettledAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    columnConfirm.setCellFactory(param -> new CheckBoxTableCell());

    tableUnsettledPayments.setItems(initFilteredPaymentsView());
    tableUnsettledPayments.setRowFactory(param -> {
      TableRow<Payment> row = new TableRow<>();
      row.setOnMouseClicked(mouseEvent -> handlePaymentRowClicked(row, mouseEvent));
      return row;
    });
  }

  private FilteredList<Payment> initFilteredPaymentsView() {
    FilteredList<Payment> filteredPayments = new FilteredList<>(unsettledPayments, payment -> true);
    buttonPaymentsPaid.setOnAction(event ->
        updatePaymentsView(filteredPayments));
    buttonPaymentsOwed.setOnAction(event ->
        updatePaymentsView(filteredPayments));
    buttonPaymentsOther.setOnAction(event ->
        updatePaymentsView(filteredPayments));
    return filteredPayments;
  }

  private void updatePaymentsView(FilteredList<Payment> filteredPayments) {
    filteredPayments.setPredicate(this::isPaymentVisibleOnList);
    updateSpentMoney();
  }

  private boolean isPaymentVisibleOnList(Payment payment) {
    if (isCurrentUserPaymentOwner(payment))
      return buttonPaymentsPaid.isSelected();
    if (payment.isUserOwing(currentUser.getId()))
      return buttonPaymentsOwed.isSelected();
    return buttonPaymentsOther.isSelected();
  }

  private void handlePaymentRowClicked(TableRow<Payment> row, MouseEvent mouseEvent) {
    if (row.isEmpty() || mouseEvent.getClickCount() != 2)
      return;
    Payment payment = row.getItem();
    if (isCurrentUserBudgetOwner() || isCurrentUserPaymentOwner(payment))
      displayPaymentWindow(payment);
    // TODO else: you have no rights to edit this payment (special color or information)
  }

  private boolean isCurrentUserBudgetOwner() {
    return Objects.equals(currentUser, budget.getOwner());
  }

  private boolean isCurrentUserPaymentOwner(Payment payment) {
    return payment.getPayerId() == currentUser.getId();
  }

  private void displayPaymentWindow(Payment payment) {
    UpdatePaymentWindow paymentWindow = new UpdatePaymentWindow(budget, payment, participantsList);
    paymentWindow.initOwner(currentStage);
    paymentWindow.setOnHidden(event -> fillTableUnsettledPayments());
    paymentWindow.show();
  }

  private void initParticipantsTable() {
    columnUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnUserMail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnUserBalance.setCellValueFactory(this::userBalanceCellFactory);

    tableParticipants.setItems(participantsList);
    tableParticipants.setRowFactory(param -> {
      TableRow<User> row = new TableRow<>();
      row.setOnMouseClicked(mouseEvent -> handleParticipantCellClicked(row, mouseEvent));
      return row;
    });
  }

  private void handleParticipantCellClicked(TableRow<User> row, MouseEvent mouseEvent) {
    if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
      final User participant = row.getItem();
      boolean hasUnsettledPayments = unsettledPayments.stream()
          .anyMatch(payment -> payment.getPayerId() == participant.getId());
      displayParticipantDetailsWindow(participant, hasUnsettledPayments);
    }
  }

  private void displayParticipantDetailsWindow(User participant, boolean hasUnsettledPayments) {
    ParticipantDetailsWindow participantWindow =
        new ParticipantDetailsWindow(budget, participant, hasUnsettledPayments);
    participantWindow.initOwner(currentStage);
    participantWindow.setOnHidden(event -> {
      fillTableParticipants();
      fillTableUnsettledPayments();
    });
    participantWindow.show();
  }

  private ObservableValue<BigDecimal> userBalanceCellFactory(CellDataFeatures<User, BigDecimal> cell) {
    final User participant = cell.getValue();
    final BigDecimal userBalance = BigDecimal.valueOf(calculateUserBalance(participant, unsettledPayments));
    return new ReadOnlyObjectWrapper<>(userBalance.setScale(2, BigDecimal.ROUND_HALF_DOWN));
  }

  void addParticipants(List<User> users) {
    users.removeAll(participantsList);
    participantsList.addAll(users);
    try {
      List<User> usersSerializable = new ArrayList<>(users);
      dbHandler.addBudgetParticipants(budget.getId(), usersSerializable);
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  public void update() {
    fillTableParticipants();
    fillTableUnsettledPayments();
  }

  private void fillTableUnsettledPayments() {
    fillTablePayments(unsettledPayments, false);
    updateSpentMoney();
  }

  private void fillTableParticipants() {
    participantsList.clear();
    try {
      participantsList.addAll(dbHandler.getBudgetParticipants(budget.getId()));
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private void fillTablePayments(ObservableList<Payment> payments, boolean settled) {
    payments.clear();
    try {
      payments.addAll(dbHandler.getAllPayments(budget.getId(), settled));
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private void updateSpentMoney() {
    refreshBalanceCells();
    spentMoneySum = unsettledPayments.stream()
        .filter(this::isPaymentVisibleOnList)
        .mapToDouble(Payment::getAmount)
        .sum();
    labelSpentMoney.setText(String.format("Sum: %.2f$", spentMoneySum));
  }

  private double calculateUserBalance(User user, List<Payment> payments) {
    double balance = 0.0;
    for (Payment payment : payments) {
      final double amount = payment.getAmount();
      final double perUser = amount / (payment.getOwingUsers().size() + 1 /* payer */);
      if (payment.getPayerId() == user.getId())
        balance += amount - perUser;
      else if (payment.isUserOwing(user.getId()))
        balance -= perUser;
    }
    return balance;
  }

  private void refreshBalanceCells() {
    tableParticipants.getColumns().get(2).setVisible(false);
    tableParticipants.getColumns().get(2).setVisible(true);
    labelCurrentUserBalance.setText(
        String.format("Balance: %.2f$", calculateUserBalance(currentUser, unsettledPayments)));
  }

  @Override
  protected void clearErrorHighlights() {

  }

  public class CheckBoxTableCell extends TableCell<Payment, Boolean> {

    private final CheckBox checkBox = new CheckBox();

    public CheckBoxTableCell() {
      setAlignment(Pos.CENTER);
      checkBox.setOnAction(event -> {
        Payment payment = (Payment) CheckBoxTableCell.this.getTableRow().getItem();
        payment.setAccept(!payment.isAccepted());
      });
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
      super.updateItem(item, empty);
      if (!empty) {
        checkBox.setSelected(true);
        setGraphic(checkBox);
        checkBox.setDisable(!isCurrentUserBudgetOwner());
      }
      else
        setGraphic(null);
    }
  }
}
