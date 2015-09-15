package client.controllers;

import common.data.BankTransfer;
import common.data.Budget;
import common.utils.DataUtils;
import common.data.Payment;
import common.data.Settlement;
import client.view.Alerts;
import client.view.StatusImageCell;
import client.windows.SettledPaymentsWindow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SettlementDetailsController extends BasicController implements Initializable {

  @FXML
  private Text textDetails;
  @FXML
  private Button buttonSetAsPaid, buttonPayments, buttonClose;

  @FXML
  private TableView<BankTransfer> tableBankTransfers;
  @FXML
  private TableColumn<BankTransfer, String> columnSender, columnRecipient, columnBankAccount;
  @FXML
  private TableColumn<BankTransfer, BigDecimal> columnAmount;
  @FXML
  private TableColumn<BankTransfer, ImageView> columnStatus;
  @FXML
  private TableColumn<Payment, Boolean> columnConfirm;

  private final ObservableList<BankTransfer> contentList = FXCollections.observableArrayList();
  private Settlement settlement;
  private Budget budget;

  public void setData(Settlement settlement, Budget budget) {
    this.settlement = settlement;
    this.budget = budget;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initButtons();
    initBankTransfersTable();
  }

  private void initButtons() {
    buttonClose.setOnAction(event -> currentStage.close());
    buttonSetAsPaid.setOnAction(event -> setAsPaid());
    buttonPayments.setOnAction(event -> displaySettledPaymentsWindow());
  }

  private void displaySettledPaymentsWindow() {
    SettledPaymentsWindow paymentsWindow = new SettledPaymentsWindow(settlement);
    paymentsWindow.showAndWait();
    fillSettlementsTable();
  }

  private void initBankTransfersTable() {
    columnSender.setCellValueFactory(new PropertyValueFactory<>("sender"));
    columnRecipient.setCellValueFactory(new PropertyValueFactory<>("recipient"));
    columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    columnBankAccount.setCellValueFactory(new PropertyValueFactory<>("bankAccount"));
    columnStatus.setCellFactory(param -> new StatusImageCell());
    columnConfirm.setCellFactory(param -> new CheckBoxTableCell());

    tableBankTransfers.setItems(contentList);
  }

  public void fillSettlementsTable() {
    contentList.clear();
    try {
      List<BankTransfer> transfers = dbHandler.getBankTransfersBySettlementId(settlement.getSettlementId());
      DataUtils.sortTransfersByStatus(transfers);
      contentList.addAll(transfers);
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  public void setAsPaid() {
    //TODO: add confirmation window
    final Map<Integer, BankTransfer.Status> bankTransfersToSet = contentList.stream()
        .filter(BankTransfer::isToUpdate)
        .peek(transfer -> transfer.updateStatus(currentUser.getId()))
        .collect(Collectors.toMap(BankTransfer::getId, BankTransfer::getStatus));
    try {
      dbHandler.setBankTransfersStatus(bankTransfersToSet);
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
    fillSettlementsTable();
  }

  @Override
  protected void clearErrorHighlights() {
  }

  public class CheckBoxTableCell extends TableCell<Payment, Boolean> {

    private final CheckBox checkBox = new CheckBox();

    public CheckBoxTableCell() {
      setAlignment(Pos.CENTER);
      checkBox.setOnAction(event -> {
        BankTransfer transfer = (BankTransfer) getTableRow().getItem();
        transfer.setToUpdate(!transfer.isToUpdate());
      });
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
      super.updateItem(item, empty);
      if (!empty) {
        checkBox.setSelected(false);
        setGraphic(checkBox);
        final BankTransfer transfer = (BankTransfer) getTableRow().getItem();
        if (transfer != null)
          updateGraphic(transfer);
      }
      else {
        setGraphic(null);
      }
    }

    private void updateGraphic(BankTransfer transfer) {
      final boolean enable = isCurrentUserBudgetOwner() || isCurrentUserTransferParticipant(transfer);
      checkBox.setDisable(!enable);
    }

    private boolean isCurrentUserBudgetOwner() {
      return Objects.equals(currentUser, budget.getOwner());
    }

    private boolean isCurrentUserTransferParticipant(BankTransfer transfer) {
      return transfer.getSenderId() == currentUser.getId()
             || transfer.getRecipientId() == currentUser.getId();
    }
  }
}
