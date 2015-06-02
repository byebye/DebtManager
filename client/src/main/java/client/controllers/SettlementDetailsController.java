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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by vsmasster on 26.05.15.
 */
public class SettlementDetailsController implements Initializable {
   //TODO: discuss if normal user can see all bank transfers
   @FXML
   Text txtDetails;
   @FXML
   Button btnSetAsPaid,btnClose;
   @FXML
   TableView tabBankTransfers;
   @FXML
   TableColumn colWho,colWhom,colAmount,colBankAccount,colConfirm,colStatus;
   private static DBHandler dbController = LoginController.dbController;
   private final ObservableList<BankTransfer> contentList = FXCollections.observableArrayList();

   private Settlement settlement;
   private Budget budget;

   public void setData(Settlement settlement,Budget budget){
      this.settlement = settlement;
      this.budget = budget;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Butons
      btnClose.setOnAction(event->{
         Stage stage = (Stage) btnClose.getScene().getWindow();
         stage.close();
      });

      btnSetAsPaid.setOnAction(event->setAsPaid());//TODO: add confirmation window

      //Columns
      colWho.setCellValueFactory(new PropertyValueFactory<BankTransfer,String>("who"));
      colWhom.setCellValueFactory(new PropertyValueFactory<BankTransfer,String>("whom"));
      colAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer,BigDecimal>("amount"));
      colBankAccount.setCellValueFactory(new PropertyValueFactory<BankTransfer,String>("bankAccount"));
      colStatus.setCellValueFactory(new PropertyValueFactory<BankTransfer,String>("status"));
      colConfirm.setCellFactory(param->new CheckBoxTableCell());
      //Table
      tabBankTransfers.setItems(contentList);
   }

   public void fillContentList(){
      try{
         contentList.addAll(dbController.getBankTransfersBySettlementId(settlement.getSettlementId()));
      }catch(Exception e){
         e.printStackTrace();
      }
   }

   public void setAsPaid(){
      List<Integer> bankTransfersToSet = new ArrayList<>();
      for(BankTransfer bk: contentList){
         if(bk.getAccept())
            bankTransfersToSet.add(bk.getId());
      }

      try{
         dbController.setBankTransferStatus(bankTransfersToSet,2);
      }catch(Exception e){
         e.printStackTrace();
      }
      contentList.clear();
      fillContentList();
   }

   public class CheckBoxTableCell extends TableCell<Payment, Boolean> {

      private final CheckBox checkBox = new CheckBox();

      public CheckBoxTableCell() {
         setAlignment(Pos.CENTER);
         checkBox.setOnAction(event -> {
            BankTransfer bk = (BankTransfer)CheckBoxTableCell.this.getTableRow().getItem();
            bk.setAccept(!bk.getAccept());
         });
      }

      @Override
      public void updateItem(Boolean item, boolean empty) {
         super.updateItem(item, empty);
         if(!empty) {
            checkBox.setSelected(false);
            setGraphic(checkBox);

            if (LoginController.currentUser.getId() == budget.getOwner().getId())
               checkBox.setDisable(false);
            else
               checkBox.setDisable(true);
         }
         else
            setGraphic(null);
      }
   }
}
