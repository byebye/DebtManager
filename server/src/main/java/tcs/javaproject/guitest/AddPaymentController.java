package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Payments;
import tcs.javaproject.database.tables.Users;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.util.ResourceBundle;

/**
 * Created by Vsmasster on 07.05.15.
 */


public class AddPaymentController implements Initializable {
    @FXML
    private Button btnAddPayment;
    @FXML
    private TextField txtFieldAmount;
    @FXML
    private TextArea txtAreaWhat;

    private Budget budget;
    private int userId;
   private BudgetWindow parent;

    public void setBudget(Budget budget){
        this.budget = budget;
    }
    public void setUser(int userId) {this.userId = userId;}
   public void setParent(BudgetWindow parent){
      this.parent = parent;
   }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAddPayment.setOnAction(event -> {
           addRecord();
           Stage stage = (Stage) btnAddPayment.getScene().getWindow();
           parent.close();
           try {
              BudgetWindow budgetWindow = new BudgetWindow(budget, userId);
              budgetWindow.show();
           }catch(Exception e){

           }
           stage.close();
        });
    }

    boolean addRecord(){
       String url = "jdbc:postgresql://localhost/debtmanager";

       try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
          DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
          final int result = create.insertInto(Payments.PAYMENTS,
                Payments.PAYMENTS.BUDGET_ID,
                Payments.PAYMENTS.AMOUNT,
                Payments.PAYMENTS.USER_ID,
                Payments.PAYMENTS.DESCRIPTION
          ).values(budget.getId(),
                BigDecimal.valueOf(Double.parseDouble(txtFieldAmount.getText())),
                userId,
                txtAreaWhat.getText())
                .execute();
       }
       catch (Exception e) {
          e.printStackTrace();
          return false;
       }
       return true;
    }
}
