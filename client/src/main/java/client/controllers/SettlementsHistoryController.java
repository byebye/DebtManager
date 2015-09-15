package client.controllers;

import common.data.Budget;
import common.data.Settlement;
import client.view.Alerts;
import client.windows.SettlementDetailsWindow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class SettlementsHistoryController extends BasicController implements Initializable {

  @FXML
  public Button buttonClose;
  @FXML
  private TableView<Settlement> tableSettleHistory;
  @FXML
  private TableColumn<Settlement, BigDecimal> columnSpentMoney;
  @FXML
  private TableColumn<Settlement, String> columnDate, columnStatus;

  private final ObservableList<Settlement> settlementsHistory = FXCollections.observableArrayList();

  private Budget budget;

  public void setBudget(Budget budget) {
    this.budget = budget;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttonClose.setOnAction(event -> currentStage.close());
    initSettlementsHistoryTable();
  }

  private void initSettlementsHistoryTable() {
    columnDate.setCellValueFactory(new PropertyValueFactory<>("settleDate"));
    columnSpentMoney.setCellValueFactory(new PropertyValueFactory<>("spentMoney"));
    columnStatus.setCellValueFactory(new PropertyValueFactory<>("statusString"));
    columnStatus.setCellFactory(param -> createStatusCellFactory());

    tableSettleHistory.setItems(settlementsHistory);
    tableSettleHistory.setRowFactory(param -> {
          TableRow<Settlement> row = new TableRow<>();
          row.setOnMouseClicked(mouseEvent -> handleSettlementCellClicked(row, mouseEvent));
          return row;
        }
    );
  }

  private TableCell<Settlement, String> createStatusCellFactory() {
    return new TableCell<Settlement, String>() {
      @Override
      public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
          final Settlement settlement = (Settlement) getTableRow().getItem();
          if (settlement != null) {
            setStyle("-fx-background-color:" + (settlement.areAllBankTransfersPaid() ? "green" : "red"));
          }
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
    settlementDetailsWindow.showAndWait();
    fillTableSettlementsHistory();
  }

  public void fillTableSettlementsHistory() {
    settlementsHistory.clear();
    try {
      List<Settlement> settlements = dbHandler.getAllSettlementsOfBudget(budget.getId());
      settlementsHistory.addAll(settlements);
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
