package tcs.javaproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tcs.javaproject.Budget;
import tcs.javaproject.windows.LoginWindow;

import java.math.BigDecimal;
import java.net.URL;
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

   public void setBudget(Budget budget) {
      this.budget = budget;
   }

   public void setUser(int userId) {
      this.userId = userId;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      btnAddPayment.setOnAction(event -> {
         BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(txtFieldAmount.getText()));
         dbController.addPayment(budget, userId, amount, txtAreaWhat.getText());
         Stage stage = (Stage) btnAddPayment.getScene().getWindow();
         stage.close();
      });
   }
}
