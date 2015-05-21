package client.controllers;

import common.Budget;
import common.DBHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class AddPaymentController implements Initializable {
   @FXML
   private Button btnAddPayment;
   @FXML
   private TextField txtFieldAmount;
   @FXML
   private TextArea txtAreaWhat;

   private static DBHandler dbController = LoginController.dbController;

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
         try {
            dbController.addPayment(budget, userId, amount, txtAreaWhat.getText());
         }
         catch (RemoteException e) {
            e.printStackTrace();
         }
         Stage stage = (Stage) btnAddPayment.getScene().getWindow();
         stage.close();
      });
   }
}
