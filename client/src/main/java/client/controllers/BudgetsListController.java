package client.controllers;

import client.windows.BudgetCreatorWindow;
import client.windows.BudgetWindow;
import client.windows.LoginWindow;
import common.Budget;
import common.DBHandler;
import common.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
   private Button btnLogout;
   @FXML
   private Button btnCreateNewBudget;
   @FXML
   private TableColumn colName, colDescription;
   @FXML
   private TableColumn colPeople;
   @FXML
   public TableColumn colOwner;
   @FXML
   private TableView<Budget> tabMyBudgets;
   @FXML
   private Text txtUserName;

   private static DBHandler dbController = LoginController.dbController;
   private final ObservableList<Budget> budgets = FXCollections.observableArrayList();
   private int userId;

   public void setUserEmail(String userName) {
      try {
         User user = dbController.getUserByEmail(userName);
         userId = user.getId();
         txtUserName.setText(user.getName());
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
   }

   public void fillBudgetsTable() {
      budgets.clear();
      try {
         budgets.addAll(dbController.getAllBudgets(userId));
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
      btnLogout.setOnAction(event -> {
         Stage stage = (Stage) btnLogout.getScene().getWindow();
         stage.close();
      });

      btnCreateNewBudget.setOnAction(event -> {
         try {
            BudgetCreatorWindow budgetCreatorWindow = new BudgetCreatorWindow(userId);
            budgetCreatorWindow.setOnHidden(e -> fillBudgetsTable());
            budgetCreatorWindow.show();
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
            return new ReadOnlyObjectWrapper(budget.getValue().getOwner().getName());
         }
      });
      tabMyBudgets.setItems(budgets);

      tabMyBudgets.setRowFactory(param -> {
         TableRow<Budget> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               Budget budget = row.getItem();
               try {
                  BudgetWindow budgetWindow = new BudgetWindow(budget, userId);
                  budgetWindow.setOnHidden(e -> fillBudgetsTable());
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
