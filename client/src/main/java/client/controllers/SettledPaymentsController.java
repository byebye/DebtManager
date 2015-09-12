package client.controllers;

import common.data.Payment;
import common.data.Settlement;
import client.view.Alerts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class SettledPaymentsController extends BasicController implements Initializable {

  @FXML
  public Button buttonClose;
  @FXML
  private TableView<Payment> tableSettledPayments;
  @FXML
  private TableColumn<Payment, String> columnSettledPayer, columnSettledDescription;
  @FXML
  private TableColumn<Payment, Integer> columnSettledAmount;

  private final ObservableList<Payment> settledPayments = FXCollections.observableArrayList();
  private Settlement settlement;

  public void setSettlement(Settlement settlement) {
    this.settlement = settlement;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttonClose.setOnAction(event -> currentStage.close());
    initSettledPaymentsTable();
  }

  private void initSettledPaymentsTable() {
    columnSettledPayer.setCellValueFactory(new PropertyValueFactory<>("payer"));
    columnSettledDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    columnSettledAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

    tableSettledPayments.setItems(settledPayments);
  }

  public void fillTableSettledPayments() {
    try {
      settledPayments.setAll(dbHandler.getPaymentsBySettlementId(settlement.getSettlementId()));
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  @Override
  protected void clearErrorHighlights() {

  }
}
