package client.controllers;

import common.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * Created by vsmasster on 26.05.15.
 */
public class SettlementDetailsController implements Initializable {
   //TODO: discuss if normal user can see all bank transfers
   @FXML
   Text txtDetails;
   @FXML
   Button btnSetAsPaid, btnClose;
   @FXML
   TableView tabBankTransfers;
   @FXML
   TableColumn colWho, colWhom, colAmount, colBankAccount, colConfirm, colStatus;
   private static DBHandler dbController = LoginController.dbController;
   private final ObservableList<BankTransfer> contentList = FXCollections.observableArrayList();

   private final User currentUser = LoginController.currentUser;
   private Settlement settlement;
   private Budget budget;

   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   public void setData(Settlement settlement, Budget budget) {
      this.settlement = settlement;
      this.budget = budget;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnClose.setOnAction(event -> currentStage.close());
      btnSetAsPaid.setOnAction(event -> setAsPaid());//TODO: add confirmation window

      //Columns
      colWho.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("who"));
      colWhom.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("whom"));
      colAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer, BigDecimal>("amount"));
      colBankAccount.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("bankAccount"));
      colStatus.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("status"));
      colConfirm.setCellFactory(param -> new CheckBoxTableCell());
      //Table
      tabBankTransfers.setItems(contentList);
   }

   public void fillContentList() {
      contentList.clear();
      try {
         contentList.addAll(dbController.getBankTransfersBySettlementId(settlement.getSettlementId()));
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void setAsPaid() {
      Map<Integer, Integer> bankTransfersToSet = new HashMap<>();
      for (BankTransfer transfer : contentList)
         if (transfer.isToUpdate()) {
            transfer.updateStatus(currentUser.getId());
            bankTransfersToSet.put(transfer.getId(), transfer.getStatus().getValue());
         }

      try {
         dbController.setBankTransfersStatus(bankTransfersToSet);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      fillContentList();
   }

   public class CheckBoxTableCell extends TableCell<Payment, Boolean> {

      private final CheckBox checkBox = new CheckBox();

      public CheckBoxTableCell() {
         setAlignment(Pos.CENTER);
         checkBox.setOnAction(event -> {
            BankTransfer transfer = (BankTransfer) CheckBoxTableCell.this.getTableRow().getItem();
            transfer.setToUpdate(!transfer.isToUpdate());
         });
      }

      @Override
      public void updateItem(Boolean item, boolean empty) {
         super.updateItem(item, empty);
         if (!empty) {
            checkBox.setSelected(false);
            setGraphic(checkBox);

            BankTransfer transfer = (BankTransfer) CheckBoxTableCell.this.getTableRow().getItem();
            if (currentUser.equals(budget.getOwner())
                || transfer.getWhoId() == currentUser.getId() || transfer.getWhomId() == currentUser.getId())
               checkBox.setDisable(false);
            else
               checkBox.setDisable(true);
         }
         else
            setGraphic(null);
      }
   }
}
