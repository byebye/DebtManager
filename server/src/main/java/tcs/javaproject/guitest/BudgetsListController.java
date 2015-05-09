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
import static org.jooq.impl.DSL.currentUser;

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

   private int userId;

   public void setUser(String user) {
      txtUserName.setText(user);
   }

   public void fillBudgetsTable() {
      final ObservableList<Budget> data = FXCollections.observableArrayList(getAllBudgets());
      tabMyBudgets.setItems(data);
   }

   @Override
   public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        /*
                               Buttons
        */
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

        /*
                               Table
        */

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
            deleteBudget(budget);
            fillBudgetsTable();
         }
      });

      tabMyBudgets.setRowFactory(param -> {
         TableRow<Budget> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               Budget budget = row.getItem();
               try {
                  BudgetWindow budgetWindow = new BudgetWindow(budget,userId);
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

   private boolean deleteBudget(Budget budget) {
      String url = "jdbc:postgresql://localhost/debtmanager";

      try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
         create.delete(UserBudget.USER_BUDGET)
                 .where(UserBudget.USER_BUDGET.BUDGET_ID.equal(budget.getId()))
                 .execute();
         create.delete(Budgets.BUDGETS)
                 .where(Budgets.BUDGETS.ID.equal(budget.getId()))
                 .execute();
         return true;
      }
      catch (Exception e) {
         e.printStackTrace();
         return false;
      }
   }

   private List<Budget> getAllBudgets() {
      String url = "jdbc:postgresql://localhost/debtmanager";

      try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
         List<Budget> budgets = new ArrayList<>();
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
         Result<Record1<Integer>> userIdResult = create
                 .select(Users.USERS.ID)
                 .from(Users.USERS)
                 .where(Users.USERS.NAME.equal(txtUserName.getText()))
                 .fetch();
         userId = userIdResult.get(0).value1();
         Result<Record1<Integer>> result = create
               .select(UserBudget.USER_BUDGET.BUDGET_ID)
               .from(UserBudget.USER_BUDGET)
               .where(UserBudget.USER_BUDGET.USER_ID.equal(userId)).fetch();

         for(Record1<Integer> budgetId: result){
            Result<Record3<Integer, String, String>> result2 = create
                  .select(Budgets.BUDGETS.ID, Budgets.BUDGETS.NAME, Budgets.BUDGETS.DESCRIPTION)
                  .from(Budgets.BUDGETS)
                  .where(Budgets.BUDGETS.ID.equal(budgetId.value1())).fetch();

            for (Record3<Integer, String, String> budget : result2) {
               int id = budget.value1();
               Result<Record1<Integer>> partNumResult = create
                     .select(count())
                     .from(UserBudget.USER_BUDGET)
                     .where(UserBudget.USER_BUDGET.BUDGET_ID.equal(id))
                     .fetch();
               String name = budget.value2();
               String description = budget.value3();
               final int partNum = partNumResult.get(0).value1();
               budgets.add(new Budget(id, name, description, partNum));
            }
         }
         return budgets;
      }
      catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
}
