package client.controllers;

import common.BankTransfer;
import common.DBHandler;
import common.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by vsmasster on 26.05.15.
 */
public class BankTransfersController implements Initializable {
   @FXML
   MenuButton menuChooseTransferType;
   @FXML
   MenuItem itemMyTransfers,itemOthersTransfers;
   @FXML
   Text txtTitle;
   @FXML
   TableView tabBankTransfers;
   @FXML
   TableColumn colBudgetName,colWho,colAmount,colAction,colStatus;
   @FXML
   Button btnClose;

   private static DBHandler dbController = LoginController.dbController;
   private final ObservableList<BankTransfer> contentList = FXCollections.observableArrayList();
   private boolean ifMyTransfersActive = false;
   private int userId;

   public void setUser(int user){
      this.userId = user;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnClose.setOnAction(event->{
         Stage stage = (Stage) btnClose.getScene().getWindow();
         stage.close();
      });
      //MenuItem
      itemMyTransfers.setOnAction(event->{
         loadMyTransfers();
         ifMyTransfersActive = true;
         txtTitle.setText("My transfers");
         tabBankTransfers.setVisible(true);
      });

      itemOthersTransfers.setOnAction(event->{
         loadOthersTransfers();
         ifMyTransfersActive = false;
         txtTitle.setText("Others transfers");
         tabBankTransfers.setVisible(true);
      });

      //Table
      colBudgetName.setCellValueFactory(new PropertyValueFactory<BankTransfer,String>("budgetName"));
      colWho.setCellValueFactory(new PropertyValueFactory<BankTransfer,String>("whoAcc"));
      colAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer,BigDecimal>("amount"));
      colStatus.setCellValueFactory(new PropertyValueFactory<BankTransfer,String>("status"));
      colAction.setCellFactory(param -> new UpdateStatusButtonCell());
      tabBankTransfers.setItems(contentList);
   }

   public void loadMyTransfers(){
      contentList.clear();
      try {
         contentList.addAll(dbController.getMyBankTransfers(userId));
      }catch(Exception e){
         e.printStackTrace();
      }
   }

   public void loadOthersTransfers(){
      contentList.clear();
      try {
         contentList.addAll(dbController.getOthersBankTransfers(userId));
      }catch(Exception e){
         e.printStackTrace();
      }
   }

   private class UpdateStatusButtonCell extends TableCell<User, Button> {
      final Button btnUpdateStatus = new Button();

      public UpdateStatusButtonCell() {
         btnUpdateStatus.setText("Update");
         btnUpdateStatus.setOnAction(event->{
            try {
               dbController.updateBankTransferStatus(((BankTransfer) UpdateStatusButtonCell.this.getTableRow().getItem()).getId(), userId);
               if(ifMyTransfersActive)
                  loadMyTransfers();
               else loadOthersTransfers();

            }catch (Exception e){
               e.printStackTrace();
            }
         });
      }

      @Override
      protected void updateItem(Button item, boolean empty) {
         super.updateItem(item, empty);
         if (!empty)
            setGraphic(btnUpdateStatus);
         else
            setGraphic(null);
      }
   }
}
