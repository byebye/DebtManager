package client.controllers;

import client.UpdateLongpollingCallbackRegistrar;
import client.windows.BankTransfersWindow;
import client.windows.BudgetCreatorWindow;
import client.windows.BudgetWindow;
import common.Budget;
import common.DBHandler;
import common.RemoteCallback;
import common.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class BudgetsListController implements Initializable {

   @FXML
   private Text txtUserName;
   @FXML
   private Button btnLogout, btnCreateNewBudget, btnRefreshList, btnManageBankTransfers;
   @FXML
   private TableColumn colName, colDescription;
   @FXML
   private TableColumn colPeople;
   @FXML
   private TableColumn colOwner;
   @FXML
   private TableView<Budget> tabMyBudgets;

   private static DBHandler dbController = LoginController.dbController;
   private final ObservableList<Budget> budgets = FXCollections.observableArrayList();
   private final User currentUser = LoginController.currentUser;

   private Stage currentStage;

   public void setStage(Stage stage) throws RemoteException {
      currentStage = stage;

      RemoteCallback rc = new RemoteCallback() {
         @Override
         public void call() throws RemoteException {
            BudgetsListController.this.update();
         }
      };

      rc.call();
      UpdateLongpollingCallbackRegistrar.registerCallbackOnServer(rc);

   }

   public void update() {
      budgets.clear();
      try {
         budgets.addAll(dbController.getAllBudgets(currentUser.getId()));
      }
      catch (RemoteException e) {
         displayUnableToConnectWithServerAlert();
         e.printStackTrace();
      }
   }

   private void displayUnableToConnectWithServerAlert() {
      Alert unableToConnectAlert = new Alert(Alert.AlertType.ERROR);
      unableToConnectAlert.setTitle("Unable to connect with server");
      unableToConnectAlert.setHeaderText("Unable to connect with server!");
      unableToConnectAlert.setContentText("Check your connection and try again.");
      unableToConnectAlert.showAndWait();
   }

   @Override
   public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
      txtUserName.setText(currentUser.getName());

      btnLogout.setOnAction(event -> currentStage.close());

      btnRefreshList.setOnAction(event -> update());

      btnManageBankTransfers.setOnAction(event -> {
         try {
            BankTransfersWindow bankTransfersWindow = new BankTransfersWindow();
            bankTransfersWindow.initOwner(currentStage);
            bankTransfersWindow.showAndWait();
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      });

      btnCreateNewBudget.setOnAction(event -> {
         try {
            BudgetCreatorWindow budgetCreatorWindow = new BudgetCreatorWindow();
            budgetCreatorWindow.initOwner(currentStage);
            budgetCreatorWindow.showAndWait();
            update();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      });

      colName.setCellValueFactory(new PropertyValueFactory<Budget, String>("name"));
      colDescription.setCellValueFactory(new PropertyValueFactory<Budget, String>("description"));
      colPeople.setCellValueFactory(new PropertyValueFactory<Budget, Integer>("partNum"));
      colOwner.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Budget, String>, ObservableValue<String>>() {
         public ObservableValue<String> call(TableColumn.CellDataFeatures<Budget, String> budget) {
            return new ReadOnlyObjectWrapper(budget.getValue().getOwner().getEmail());
         }
      });
      tabMyBudgets.setItems(budgets);

      tabMyBudgets.setRowFactory(param -> {
         TableRow<Budget> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               Budget budget = row.getItem();
               try {
                  BudgetWindow budgetWindow = new BudgetWindow(budget);
                  budgetWindow.setOnHidden(e -> update());
                  budgetWindow.initOwner(currentStage);
                  budgetWindow.show();
               }
               catch (IOException e) {
                  e.printStackTrace();
               }
            }
         });
         return row;
      });
   }
}
