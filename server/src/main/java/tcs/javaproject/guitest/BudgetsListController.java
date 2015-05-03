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

    public void setUserName(String userName) {
        txtUserName.setText(userName);
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
                BudgetCreatorWindow budgetCreatorWindow = new BudgetCreatorWindow();
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
                new PropertyValueFactory<Budget,Integer>("partNum")
        );

        tabMyBudgets.setRowFactory(param -> {
            TableRow<Budget> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Budget budget = row.getItem();
                    try {
                        BudgetWindow budgetWindow = new BudgetWindow(budget.getName());
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

    private List<Budget> getAllBudgets() {
        String url = "jdbc:postgresql://localhost/debtmanager";

        try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
            DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
            Result<Record1<Integer>> userIdResult = create
                    .select(Users.USERS.ID)
                    .from(Users.USERS)
                    .where(Users.USERS.NAME.equal(txtUserName.getText()))
                    .fetch();
            final int userId = userIdResult.get(0).value1();
            Result<Record3<Integer, String, String>> result = create
                    .select(Budgets.BUDGETS.ID, Budgets.BUDGETS.NAME, Budgets.BUDGETS.DESCRIPTION)
                    .from(Budgets.BUDGETS)
                    .where(Budgets.BUDGETS.OWNER_ID.equal(userId)).fetch();
            List<Budget> budgets = new ArrayList<>();
            for (Record3<Integer, String, String> budget : result) {
                int id = budget.value1();
                Result<Record1<Integer>> partNumResult = create
                        .select(count())
                        .from(UserBudget.USER_BUDGET)
                        .where(UserBudget.USER_BUDGET.BUDGET_ID.equal(id))
                        .fetch();
                String name = budget.value2();
                String description = budget.value3();
                final int partNum = partNumResult.get(0).value1();
                budgets.add(new Budget(name, description, partNum));
            }
            return budgets;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
