package client.controllers;

import common.BankTransfer;
import common.Budget;
import common.DBHandler;
import common.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SettleController implements Initializable {

   @FXML
   TableView<BankTransfer> tabSettleView;
   @FXML
   TableColumn colWho, colWhom, colAmount, colAccNumber;
   @FXML
   Button btnConfirm, btnDecline;
   @FXML
   CheckBox checkBoxSendViaMail;

   private static DBHandler dbController = LoginController.dbController;
   private static ObservableList<BankTransfer> bankTransfers = FXCollections.observableArrayList();
   private Budget budget;
   private BudgetController parentController;
   private ObservableList<Payment> paymentsToSettle;

   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   public void setBudget(Budget budget, ObservableList<Payment> paymentsToSettle, BudgetController parentController) {
      this.budget = budget;
      this.paymentsToSettle = paymentsToSettle;
      this.parentController = parentController;
   }

   public void fillAllTables() {
      try {
         List<Payment> paymentsSerializable = new ArrayList<>(paymentsToSettle);
         bankTransfers.addAll(dbController.calculateBankTransfers(budget.getId(), paymentsSerializable));
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
      tabSettleView.setItems(bankTransfers);
   }

   public void clearTable() {
      bankTransfers.clear();
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      colWho.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("who"));
      colWhom.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("whom"));
      colAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer, BigDecimal>("amount"));
      colAccNumber.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("bankAccount"));

      btnConfirm.setOnAction(event -> {
         try {
            List<Payment> paymentsSerializable = new ArrayList<>(paymentsToSettle);
            List<BankTransfer> bankTransfersSerializable = new ArrayList<>(bankTransfers);
            dbController.settleUnaccountedPayments(budget.getId(), paymentsSerializable, bankTransfersSerializable,
                                                   checkBoxSendViaMail.isSelected());
            parentController.fillAllTables();
            bankTransfers.clear();
            currentStage.close();
         }
         catch (RemoteException e) {
            e.printStackTrace();
         }
      });

      btnDecline.setOnAction(event -> {
         bankTransfers.clear();
         currentStage.close();
      });
   }
}
