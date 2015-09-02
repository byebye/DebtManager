package client.controllers;

import client.view.Alerts;
import client.windows.BankTransfersWindow;
import client.windows.BudgetCreatorWindow;
import client.windows.BudgetWindow;
import common.connection.DbHandler;
import common.data.Budget;
import common.data.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetsListController extends BasicController implements Initializable, SelfUpdating {

  @FXML
  private Label labelUserName;
  @FXML
  private Button buttonLogout, buttonCreateNewBudget, buttonRefreshList, buttonManageBankTransfers;
  @FXML
  private TableColumn<Budget, String> columnName, columnDescription, columnOwner;
  @FXML
  private TableColumn<Budget, Integer> columnParticipantsCount;
  @FXML
  private TableView<Budget> tableBudgets;

  private final ObservableList<Budget> budgets = FXCollections.observableArrayList();

  public void update() {
    try {
      final List<Budget> currentBudgets = dbHandler.getAllBudgets(currentUser.getId());
      budgets.setAll(currentBudgets);
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  @Override
  public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    labelUserName.setText(currentUser.getName());
    initButtons();
    initColumns();
    initTable();
  }

  private void initButtons() {
    buttonLogout.setOnAction(event -> currentStage.close());
    buttonRefreshList.setOnAction(event -> update());
    buttonManageBankTransfers.setOnAction(event -> displayBankTransfersWindow());
    buttonCreateNewBudget.setOnAction(event -> {
      displayBudgetCreatorWindow();
      update();
    });
  }

  private void initColumns() {
    columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    columnParticipantsCount.setCellValueFactory(new PropertyValueFactory<>("partNum"));
    columnOwner.setCellValueFactory(budget ->
        new ReadOnlyObjectWrapper<>(budget.getValue().getOwner().getEmail()));
  }

  private void initTable() {
    tableBudgets.setItems(budgets);
    tableBudgets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tableBudgets.setRowFactory(param -> {
      TableRow<Budget> row = new TableRow<>();
      row.setOnMouseClicked(mouseEvent -> {
        if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
          Budget budget = row.getItem();
          displayBudgetWindow(budget);
        }
      });
      return row;
    });
  }

  private void displayBudgetWindow(Budget budget) {
    BudgetWindow budgetWindow = new BudgetWindow(budget);
    budgetWindow.setOnHidden(event -> update());
    budgetWindow.initOwner(currentStage);
    budgetWindow.show();
  }

  private void displayBankTransfersWindow() {
    BankTransfersWindow bankTransfersWindow = new BankTransfersWindow();
    bankTransfersWindow.initOwner(currentStage);
    bankTransfersWindow.showAndWait();
  }

  private void displayBudgetCreatorWindow() {
    BudgetCreatorWindow budgetCreatorWindow = new BudgetCreatorWindow();
    budgetCreatorWindow.initOwner(currentStage);
    budgetCreatorWindow.showAndWait();
  }

  @Override
  protected void clearErrorHighlights() {

  }
}
