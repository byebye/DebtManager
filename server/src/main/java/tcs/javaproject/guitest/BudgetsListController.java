package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jooq.*;
import org.jooq.impl.DSL;
import tcs.javaproject.database.DatabaseController;
import tcs.javaproject.database.tables.Budgets;
import tcs.javaproject.database.tables.UserBudget;
import tcs.javaproject.database.tables.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.jooq.impl.DSL.count;

public class BudgetsListController implements Initializable {

   @FXML
   private Button btnLogout;
   @FXML
   private Button btnCreateNewBudget;
   @FXML
   private TableColumn colName, colCom;
   @FXML
   private TableColumn colPeop;
   @FXML
   private TableView<Budget> tabMyBudgets;
   @FXML
   private Text txtUserName;

   private final DatabaseController dbController = LoginWindow.dbController;

   private int userId;

   public void setUserEmail(String userName) {
      User user = dbController.getUserByEmail(userName);
      userId = user.getId();
      txtUserName.setText(user.getName());
   }

   public void fillBudgetsTable() {
      final ObservableList<Budget> data = FXCollections.observableArrayList(dbController.getAllBudgets(userId));
      tabMyBudgets.setItems(data);
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

      colName.setCellValueFactory(
              new PropertyValueFactory<Budget, String>("name")
      );
      colCom.setCellValueFactory(
              new PropertyValueFactory<Budget, String>("description")
      );
      colPeop.setCellValueFactory(
              new PropertyValueFactory<Budget, Integer>("partNum")
      );

      tabMyBudgets.setOnKeyPressed(keyEvent -> {
         if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            Budget budget = tabMyBudgets.getSelectionModel().getSelectedItem();
            dbController.deleteBudget(budget);
            fillBudgetsTable();
         }
      });

      tabMyBudgets.setRowFactory(param -> {
         TableRow<Budget> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               Budget budget = row.getItem();
               try {
                  BudgetWindow budgetWindow = new BudgetWindow(budget);
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
