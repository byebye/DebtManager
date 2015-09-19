package client.controllers;

import common.data.BankTransfer;
import common.data.Budget;
import common.data.Payment;
import common.data.User;
import client.view.Alerts;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SettleController extends BasicController implements Initializable {

  @FXML
  private Button buttonConfirm, buttonDecline;
  @FXML
  private TableView<BankTransfer> tableSettleView;
  @FXML
  private TableColumn<BankTransfer, String> columnSender, columnRecipient, columnAccountNumber;
  @FXML
  private TableColumn<BankTransfer, BigDecimal> columnAmount;
  @FXML
  private CheckBox checkBoxSendViaMail;

  private List<BankTransfer> bankTransfers;
  private List<User> participants;
  private List<Payment> paymentsToSettle;
  private BudgetController budgetController;
  private Budget budget;

  public void setBudgetController(BudgetController budgetController) {
    this.budgetController = budgetController;
  }

  public void setBudget(Budget budget) {
    this.budget = budget;
  }

  public void setParticipants(List<User> participants) {
    this.participants = participants;
  }

  public void setPaymentsToSettle(List<Payment> paymentsToSettle) {
    this.paymentsToSettle = paymentsToSettle;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initButtons();
    initColumns();
  }

  private void initButtons() {
    buttonConfirm.setOnAction(event -> acceptSettlement());
    buttonDecline.setOnAction(event -> currentStage.close());
  }

  private void initColumns() {
    columnSender.setCellValueFactory(new PropertyValueFactory<>("sender"));
    columnRecipient.setCellValueFactory(new PropertyValueFactory<>("recipient"));
    columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    columnAccountNumber.setCellValueFactory(new PropertyValueFactory<>("bankAccount"));
  }

  public void fillBankTransfersTable() {
    bankTransfers = calculateBankTransfers(paymentsToSettle);
    tableSettleView.setItems(FXCollections.observableArrayList(bankTransfers));
  }

  private List<BankTransfer> calculateBankTransfers(List<Payment> paymentsToSettle) {
    final Map<Integer, User> idToUser = participants.stream()
        .collect(Collectors.toMap(User::getId, user -> user));
    final List<Integer> usersBelowAverage = new LinkedList<>();
    final List<Integer> usersAboveAverage = new LinkedList<>();
    final Map<Integer, Double> userIdToSpentMoney = new HashMap<>();

    for (User participant : participants) {
      userIdToSpentMoney.put(participant.getId(), 0.0);
    }
    double sum = 0;
    for (Payment p : paymentsToSettle) {
      sum += p.getAmount();
      userIdToSpentMoney.put(p.getPayerId(), userIdToSpentMoney.get(p.getPayerId()) + p.getAmount());
    }

    final double average = sum / participants.size();
    for (Entry<Integer, Double> userIdAmountEntry : userIdToSpentMoney.entrySet()) {
      final int userId = userIdAmountEntry.getKey();
      final double spentAmount = userIdAmountEntry.getValue();
      if (spentAmount < average)
        usersBelowAverage.add(userId);
      else if (spentAmount > average)
        usersAboveAverage.add(userId);
    }

    List<BankTransfer> neededTransfers = new ArrayList<>();

    while (!usersBelowAverage.isEmpty() && !usersAboveAverage.isEmpty()) {
      final int userBelow = usersBelowAverage.remove(0);
      final int userAbove = usersAboveAverage.remove(0);
      final User sender = idToUser.get(userBelow);
      final User recipient = idToUser.get(userAbove);
      final double amount = average - userIdToSpentMoney.get(userBelow);
      neededTransfers.add(new BankTransfer(sender, recipient, BigDecimal.valueOf(amount)));

      final double updatedSpentMoney = userIdToSpentMoney.get(userAbove) - amount;
      userIdToSpentMoney.put(userAbove, updatedSpentMoney);
      if (updatedSpentMoney < average)
        usersBelowAverage.add(userAbove);
      else if (updatedSpentMoney > average)
        usersAboveAverage.add(userAbove);
    }
    return neededTransfers;
  }

  private void acceptSettlement() {
    try {
      dbHandler.settlePayments(budget.getId(), mapPaymentsToIds(paymentsToSettle), bankTransfers,
          checkBoxSendViaMail.isSelected());
      budgetController.update();
      currentStage.close();
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private List<Integer> mapPaymentsToIds(List<Payment> payments) {
    return payments.stream()
            .map(Payment::getId)
            .collect(Collectors.toList());
  }

  @Override
  protected void clearErrorHighlights() {
  }
}
