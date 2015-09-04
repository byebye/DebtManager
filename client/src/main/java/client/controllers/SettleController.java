package client.controllers;

import common.data.BankTransfer;
import common.data.Budget;
import common.data.Payment;
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
import java.util.List;
import java.util.ResourceBundle;

public class SettleController extends BasicController implements Initializable {

  @FXML
  private Button buttonConfirm, buttonDecline;
  @FXML
  private TableView<BankTransfer> tableSettleView;
  @FXML
  private TableColumn<BankTransfer, String> columnWho, columnWhom, columnAccountNumber;
  @FXML
  private TableColumn<BankTransfer, BigDecimal> columnAmount;
  @FXML
  private CheckBox checkBoxSendViaMail;

  private List<BankTransfer> bankTransfers;
  private List<Payment> paymentsToSettle;
  private BudgetController budgetController;
  private Budget budget;

  public void setBudgetController(BudgetController budgetController) {
    this.budgetController = budgetController;
  }

  public void setBudget(Budget budget) {
    this.budget = budget;
  }

  public void setPaymentsToSettle(List<Payment> paymentsToSettle) {
    this.paymentsToSettle = paymentsToSettle;
  }

  public void fillBankTransfersTable() {
    try {
      bankTransfers = dbHandler.calculateBankTransfers(budget.getId(), paymentsToSettle);
      tableSettleView.setItems(FXCollections.observableArrayList(bankTransfers));
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
      currentStage.close();
    }
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

  private void acceptSettlement() {
    try {
      dbHandler.settleUnaccountedPayments(budget.getId(), paymentsToSettle, bankTransfers,
          checkBoxSendViaMail.isSelected());
      budgetController.update();
      currentStage.close();
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private void initColumns() {
    columnWho.setCellValueFactory(new PropertyValueFactory<>("who"));
    columnWhom.setCellValueFactory(new PropertyValueFactory<>("whom"));
    columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    columnAccountNumber.setCellValueFactory(new PropertyValueFactory<>("bankAccount"));
  }

  @Override
  protected void clearErrorHighlights() {
  }
}
