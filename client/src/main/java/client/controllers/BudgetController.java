package client.controllers;

import client.BudgetExporter;
import client.view.Alerts;
import client.windows.AddParticipantsWindow;
import client.windows.AddPaymentWindow;
import client.windows.ParticipantDetailsWindow;
import client.windows.SettleWindow;
import client.windows.SettlementDetailsWindow;
import client.windows.UpdatePaymentWindow;
import common.connection.DbHandler;
import common.data.Budget;
import common.data.Payment;
import common.data.Settlement;
import common.data.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BudgetController extends BasicController implements Initializable, SelfUpdating {

  @FXML
  public Label labelBudgetName, labelBudgetDescription;
  @FXML
  private Button buttonBudgetClose, buttonBudgetDelete, buttonBudgetExport;
  @FXML
  private Button buttonAddPayment, buttonAddParticipant, buttonSettle;
  @FXML
  private Label labelSum, labelSumPerPerson;
  @FXML
  private TableView<Payment> tableUnaccPayments, tableAccPayments;
  @FXML
  private TableView<User> tableParticipants;
  @FXML
  private TableView<Settlement> tableSettleHistory;
  @FXML
  private TableColumn<Settlement, String> columnDate, columnStatus;
  @FXML
  private TableColumn<User, BigDecimal> columnUserBalance;
  @FXML
  private TableColumn<Settlement, Double> columnAmount;
  @FXML
  private TableColumn<Payment, String> columnAccWho, columnUnaccWho, columnUnaccWhat, columnAccWhat;
  @FXML
  private TableColumn<Payment, Integer> columnAccAmount, columnUnaccAmount;
  @FXML
  private TableColumn<User, String> columnUserName, columnUserMail;
  @FXML
  private TableColumn<Payment, Boolean> columnConfirm;

  private final ObservableList<User> participantsList = FXCollections.observableArrayList();
  private final ObservableList<Payment> accountedPayments = FXCollections.observableArrayList();
  private final ObservableList<Payment> unaccountedPayments = FXCollections.observableArrayList();
  private final ObservableList<Settlement> settleHistory = FXCollections.observableArrayList();
  private Budget budget;
  double spentMoneySum = 0;

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
  }

  private void exportBudget() {
    BudgetExporter budgetExporter = new BudgetExporter(budget,
        participantsList,
        accountedPayments,
        unaccountedPayments,
        settleHistory,
        currentStage);
    budgetExporter.export();
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
    addParticipantsWindow.setOnHidden(event -> fillTableUnaccountedPayments());
    addParticipantsWindow.show();
  }

  private void displayAddPaymentWindow() {
    AddPaymentWindow addPaymentWindow = new AddPaymentWindow(budget, participantsList);
    addPaymentWindow.initOwner(currentStage);
    addPaymentWindow.setOnHidden(event -> fillTableUnaccountedPayments());
    addPaymentWindow.show();
  }

  private void settlePayments() {
    try {
      displaySettleWindow();
      fillTableSettleHistory();
    }
    catch (Exception e) {
      e.printStackTrace();
      // TODO display proper message
    }
  }

  private void displaySettleWindow() {
    SettleWindow settleWindow = new SettleWindow(this, budget, getPaymentsToSettle());
    settleWindow.initOwner(currentStage);
    settleWindow.showAndWait();
  }

  private List<Payment> getPaymentsToSettle() {
    return unaccountedPayments.stream()
        .filter(Payment::isAccepted)
        .collect(Collectors.toList());
  }

  private void initTables() {
    initUnaccountedPaymentsTable();
    initAccountedPaymentsTable();
    initParticipantsTable();
    initSettlementsHistoryTable();
  }

  private void initUnaccountedPaymentsTable() {
    columnUnaccWho.setCellValueFactory(new PropertyValueFactory<>("who"));
    columnUnaccWhat.setCellValueFactory(new PropertyValueFactory<>("what"));
    columnUnaccAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    columnConfirm.setCellFactory(param -> new CheckBoxTableCell());

    tableUnaccPayments.setItems(unaccountedPayments);
    tableUnaccPayments.setRowFactory(param -> {
      TableRow<Payment> row = new TableRow<>();
      row.setOnMouseClicked(mouseEvent -> handlePaymentRowClicked(row, mouseEvent));
      return row;
    });
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
    return payment.getUserId() == currentUser.getId();
  }

  private void displayPaymentWindow(Payment payment) {
    UpdatePaymentWindow paymentWindow = new UpdatePaymentWindow(budget, payment, participantsList);
    paymentWindow.initOwner(currentStage);
    paymentWindow.setOnHidden(event -> fillTableUnaccountedPayments());
    paymentWindow.show();
  }

  private void initAccountedPaymentsTable() {
    columnAccWho.setCellValueFactory(new PropertyValueFactory<>("who"));
    columnAccWhat.setCellValueFactory(new PropertyValueFactory<>("what"));
    columnAccAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

    tableAccPayments.setItems(accountedPayments);
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
      boolean hasUnaccountedPayments = unaccountedPayments.stream()
          .anyMatch(payment -> payment.getUserId() == participant.getId());
      displayParticipantDetailsWindow(participant, hasUnaccountedPayments);
    }
  }

  private void displayParticipantDetailsWindow(User participant, boolean hasUnaccountedPayments) {
    ParticipantDetailsWindow participantWindow =
        new ParticipantDetailsWindow(budget, participant, hasUnaccountedPayments);
    participantWindow.initOwner(currentStage);
    participantWindow.setOnHidden(event -> {
      fillTableParticipants();
      fillTableUnaccountedPayments();
    });
    participantWindow.show();
  }

  private ObservableValue<BigDecimal> userBalanceCellFactory(CellDataFeatures<User, BigDecimal> cell) {
    final User participant = cell.getValue();
    final double balance = participant.getSpentMoney() - spentMoneySum / participantsList.size();
    return new ReadOnlyObjectWrapper<>(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_FLOOR));
  }

  private void initSettlementsHistoryTable() {
    columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    columnStatus.setCellFactory(param -> getStatusCellFactory());

    tableSettleHistory.setItems(settleHistory);
    tableSettleHistory.setRowFactory(param -> {
          TableRow<Settlement> row = new TableRow<>();
          row.setOnMouseClicked(mouseEvent -> handleSettlementCellClicked(row, mouseEvent));
          return row;
        }
    );
  }

  private TableCell<Settlement, String> getStatusCellFactory() {
    return new TableCell<Settlement, String>() {
      @Override
      public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
          setStyle(item.equals("OK") ? "-fx-background-color:green" : "-fx-background-color:red");
          setText(item);
        }
      }
    };
  }

  private void handleSettlementCellClicked(TableRow<Settlement> row, MouseEvent mouseEvent) {
    if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
      Settlement settlement = row.getItem();
      displaySettlementWindow(settlement);
    }
  }

  private void displaySettlementWindow(Settlement settlement) {
    SettlementDetailsWindow settlementDetailsWindow = new SettlementDetailsWindow(settlement, budget);
    settlementDetailsWindow.initOwner(currentStage);
    settlementDetailsWindow.setOnHidden(event -> fillTableSettleHistory());
    settlementDetailsWindow.show();
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
    fillTableUnaccountedPayments();
    fillTableAccountedPayments();
    fillTableSettleHistory();
  }

  private void fillTableAccountedPayments() {
    fillTablePayments(accountedPayments, true);
  }

  private void fillTableUnaccountedPayments() {
    fillTablePayments(unaccountedPayments, false);
    updateSpentMoneySums();
  }

  void fillTableSettleHistory() {
    settleHistory.clear();
    try {
      List<Settlement> settlements = dbHandler.getAllSettlements(budget.getId());
      sortSettlementsByStatus(settlements);
      settleHistory.addAll(settlements);
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private void sortSettlementsByStatus(List<Settlement> settlements) {
    int it = 0;
    for (int i = 0; i < settlements.size(); i++) {
      if (!Objects.equals(settlements.get(i).getStatus(), "OK"))
        Collections.swap(settlements, it++, i);
    }
  }

  void fillTableParticipants() {
    participantsList.clear();
    try {
      participantsList.addAll(dbHandler.getBudgetParticipants(budget.getId()));
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  void fillTablePayments(ObservableList<Payment> payments, boolean accounted) {
    payments.clear();
    try {
      payments.addAll(dbHandler.getAllPayments(budget.getId(), accounted));
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private void updateSpentMoneySums() {
    spentMoneySum = 0;
    participantsList.forEach(p -> p.setSpentMoney(0));
    for (Payment payment : unaccountedPayments) {
      spentMoneySum += payment.getAmount();
      Optional<User> userOptional = participantsList.stream()
          .filter(user -> user.getId() == payment.getUserId())
          .findFirst();
      if (userOptional.isPresent()) {
        User participant = userOptional.get();
        participant.addSpentMoney(payment.getAmount());
      }
      // TODO else: error - payment with owner not participating in budget
    }
    refreshBalanceCells();
    labelSum.setText(String.format("Sum: %.2f$", spentMoneySum));
    labelSumPerPerson.setText(String.format("Sum / Person: %.2f$", spentMoneySum / participantsList.size()));
  }

  private void refreshBalanceCells() {
    tableParticipants.getColumns().get(2).setVisible(false);
    tableParticipants.getColumns().get(2).setVisible(true);
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
