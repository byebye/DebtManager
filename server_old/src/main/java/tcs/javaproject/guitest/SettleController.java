package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tcs.javaproject.database.DatabaseController;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Vsmasster on 12.05.15.
 */
public class SettleController implements Initializable {
   @FXML
   TableView<BankTransfer> tabSettleView;
   @FXML
   TableColumn colWho,colWhom,colAmount,colAccNumber;
   @FXML
   Button btnConfirm, btnDecline;

   private final DatabaseController dbController = LoginWindow.dbController;
   private static ObservableList<BankTransfer> bankTransfers = FXCollections.observableArrayList();
   private Budget budget;
   private BudgetController parentController;
   private ObservableList<Payment> paymentsToSettle;

   public void setBudget(Budget budget,ObservableList<Payment> paymentsToSettle,BudgetController parentController){
      this.budget = budget;
      this.paymentsToSettle = paymentsToSettle;
      this.parentController = parentController;
   }

   public void fillAllTables(){
      bankTransfers.addAll(dbController.calculateBankTransfers(budget.getId(), paymentsToSettle));
      tabSettleView.setItems(bankTransfers);
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      colWho.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("who"));
      colWhom.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("whom"));
      colAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer, BigDecimal>("amount"));
      colAccNumber.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("bankAccount"));

      btnConfirm.setOnAction(event -> {
         dbController.settleUnaccountedPayments(budget.getId(), paymentsToSettle);
         parentController.fillAllTables();
         bankTransfers.clear();
         Stage stage = (Stage) btnConfirm.getScene().getWindow();
         stage.close();
      });

      btnDecline.setOnAction(event->{
         bankTransfers.clear();
         Stage stage = (Stage) btnDecline.getScene().getWindow();
         stage.close();
      });
   }
}
