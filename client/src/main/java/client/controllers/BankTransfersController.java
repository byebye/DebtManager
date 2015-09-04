package client.controllers;

import common.data.BankTransfer;
import common.data.BankTransfer.Status;
import common.utils.DataUtils;
import client.utils.ImageUtils;
import client.view.Alerts;
import client.view.StatusImageCell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class BankTransfersController extends BasicController implements Initializable, SelfUpdating {

  @FXML
  private Button buttonClose, buttonRefresh;

  @FXML
  private TableView<BankTransfer> tableTransfersToSend;
  @FXML
  private TableColumn<BankTransfer, String> columnSendTo, columnSendAccount, columnSendBudget;
  @FXML
  private TableColumn<BankTransfer, BigDecimal> columnSendAmount;
  @FXML
  private TableColumn<BankTransfer, ImageView> columnSendStatus;
  @FXML
  private TableColumn<BankTransfer, Boolean> columnSendUpdate;

  @FXML
  private TableView<BankTransfer> tableTransfersToReceive;
  @FXML
  private TableColumn<BankTransfer, String> columnReceiveFrom, columnReceiveAccount, columnReceiveBudget;
  @FXML
  private TableColumn<BankTransfer, BigDecimal> columnReceiveAmount;
  @FXML
  private TableColumn<BankTransfer, ImageView> columnReceiveStatus;
  @FXML
  private TableColumn<BankTransfer, Boolean> columnReceiveUpdate;

  private final ObservableList<BankTransfer> transfersToSend = FXCollections.observableArrayList();
  private final ObservableList<BankTransfer> transfersToReceive = FXCollections.observableArrayList();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initButtons();
    initTableTransfersToSend();
    initTableTransfersToReceive();
    update();
  }

  private void initButtons() {
    buttonClose.setOnAction(event -> currentStage.close());
    buttonRefresh.setOnAction(event -> update());
  }

  private void initTableTransfersToReceive() {
    columnReceiveFrom.setCellValueFactory(new PropertyValueFactory<>("who"));
    columnReceiveAccount.setCellValueFactory(new PropertyValueFactory<>("account"));
    columnReceiveAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    columnReceiveBudget.setCellValueFactory(new PropertyValueFactory<>("budgetName"));
    columnReceiveStatus.setCellFactory(param -> new StatusImageCell());
    columnReceiveUpdate.setCellFactory(param -> new UpdateStatusButtonCell("Ok.png", false));
    tableTransfersToReceive.setItems(transfersToReceive);
  }

  private void initTableTransfersToSend() {
    columnSendTo.setCellValueFactory(new PropertyValueFactory<>("whom"));
    columnSendAccount.setCellValueFactory(new PropertyValueFactory<>("account"));
    columnSendAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    columnSendBudget.setCellValueFactory(new PropertyValueFactory<>("budgetName"));
    columnSendStatus.setCellFactory(param -> new StatusImageCell());
    columnSendUpdate.setCellFactory(param -> new UpdateStatusButtonCell("Pay.png", true));
    tableTransfersToSend.setItems(transfersToSend);
  }

  @Override
  public void update() {
    loadTransfersToReceive();
    loadTransfersToSend();
  }

  public void loadTransfersToSend() {
    transfersToSend.clear();
    try {
      List<BankTransfer> transfers = dbHandler.getToSendBankTransfers(currentUser.getId());
      DataUtils.sortTransfersByStatus(transfers);
      transfersToSend.setAll(transfers);
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  public void loadTransfersToReceive() {
    transfersToReceive.clear();
    try {
      List<BankTransfer> transfers = dbHandler.getToReceiveBankTransfers(currentUser.getId());
      DataUtils.sortTransfersByStatus(transfers);
      transfersToReceive.addAll(transfers);
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  @Override
  protected void clearErrorHighlights() {
  }

  private class UpdateStatusButtonCell extends TableCell<BankTransfer, Boolean> {

    private final Button buttonUpdateStatus;
    private final boolean tableWithTransfersToSend;

    public UpdateStatusButtonCell(String imageButtonPath, boolean tableWithTransfersToSend) {
      buttonUpdateStatus = ImageUtils.loadImageButton(imageButtonPath);
      this.tableWithTransfersToSend = tableWithTransfersToSend;
      setPadding(new Insets(0, 0, 0, 0));
      buttonUpdateStatus.setOnAction(event -> handleUpdateStatus());
    }

    private void handleUpdateStatus() {
      try {
        final BankTransfer transfer = (BankTransfer) getTableRow().getItem();
        transfer.updateStatus(currentUser.getId());
        dbHandler.setBankTransfersStatus(transfer.getId(), transfer.getStatus().getValue());
        if (tableWithTransfersToSend)
          loadTransfersToSend();
        else
          loadTransfersToReceive();
      }
      catch (RemoteException e) {
        e.printStackTrace();
        Alerts.serverConnectionError();
      }
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
      super.updateItem(item, empty);
      if (!empty) {
        final BankTransfer transfer = (BankTransfer) getTableRow().getItem();
        if (transfer != null)
          updateGraphic(transfer);
      }
      else {
        setGraphic(null);
      }
    }

    private void updateGraphic(BankTransfer transfer) {
      final Status status = transfer.getStatus();
      final boolean disable = transferSent(status) || transferReceived(status);
      buttonUpdateStatus.setDisable(disable);
      setGraphic(buttonUpdateStatus);
    }

    private boolean transferSent(Status status) {
      return tableWithTransfersToSend && status != Status.NotPaid;
    }

    private boolean transferReceived(Status status) {
      return !tableWithTransfersToSend && status == Status.Confirmed;
    }
  }
}
