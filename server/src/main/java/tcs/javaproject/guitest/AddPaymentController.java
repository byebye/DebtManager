package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tcs.javaproject.database.DatabaseController;
import tcs.javaproject.database.tables.Payments;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class AddPaymentController implements Initializable {
   @FXML
   private Button btnAddPayment;
   @FXML
   private TextField txtFieldAmount;
   @FXML
   private TextArea txtAreaWhat;

   private final DatabaseController dbController = LoginWindow.dbController;

   private Budget budget;
   private int userId;
   private BudgetWindow parent;

   public void setBudget(Budget budget) {
      this.budget = budget;
   }

   public void setUser(int userId) {
      this.userId = userId;
   }

   public void setParent(BudgetWindow parent) {
      this.parent = parent;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      btnAddPayment.setOnAction(event -> {
         BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(txtFieldAmount.getText()));
         dbController.addPayment(budget, userId, amount, txtAreaWhat.getText());
         parent.getController().fillTabUnaccPayments();
         Stage stage = (Stage) btnAddPayment.getScene().getWindow();
         stage.close();
      });
   }
}
