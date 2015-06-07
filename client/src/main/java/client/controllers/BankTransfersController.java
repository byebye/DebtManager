package client.controllers;

import common.BankTransfer;
import common.DBHandler;
import common.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class BankTransfersController implements Initializable {
   @FXML
   TableView tabToSend;
   @FXML
   TableColumn colSendTo, colSendAccount, colSendAmount, colSendBudget, colSendStatus, colSendUpdate;
   @FXML
   TableView tabToReceive;
   @FXML
   TableColumn colReceiveFrom, colReceiveAccount, colReceiveAmount, colReceiveBudget, colReceiveStatus, colReceiveUpdate;
   @FXML
   Button btnClose, btnRefresh;

   private static DBHandler dbController = LoginController.dbController;
   private final ObservableList<BankTransfer> toSendTransfers = FXCollections.observableArrayList();
   private final ObservableList<BankTransfer> toReceiveTransfers = FXCollections.observableArrayList();
   private final User currentUser = LoginController.currentUser;

   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnClose.setOnAction(event -> currentStage.close());
      btnRefresh.setOnAction(event -> {
         loadToSendTransfers();
         loadToReceiveTransfers();
      });

      //Table
      colSendTo.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("whom"));
      colSendAccount.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("account"));
      colSendAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer, BigDecimal>("amount"));
      colSendBudget.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("budgetName"));
      colSendStatus.setCellFactory(param -> new StatusImageCell());
      colSendUpdate.setCellFactory(param -> new UpdateStatusButtonCell(loadImage("pay.png"), true));
      tabToSend.setItems(toSendTransfers);

      colReceiveFrom.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("who"));
      colReceiveAccount.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("account"));
      colReceiveAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer, BigDecimal>("amount"));
      colReceiveBudget.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("budgetName"));
      colReceiveStatus.setCellFactory(param -> new StatusImageCell());
      colReceiveUpdate.setCellFactory(param -> new UpdateStatusButtonCell(loadImage("ok.png"), false));
      tabToReceive.setItems(toReceiveTransfers);

      loadToSendTransfers();
      loadToReceiveTransfers();
   }

   private ImageView loadImage(String imageName) {
      Image image = new Image(getClass().getResourceAsStream("/graphics/" + imageName));
      ImageView imageView = new ImageView(image);
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(15);
      return imageView;
   }

   public void update(){
      loadToReceiveTransfers();
      loadToSendTransfers();
   }

   public void loadToSendTransfers() {
      toSendTransfers.clear();
      try {
         List<BankTransfer> transfers = dbController.getToSendBankTransfers(currentUser.getId());
         int it = 0;
         for(int i=0;i<transfers.size();i++)
            if(transfers.get(i).getStatus().getValue() == 0)
               Collections.swap(transfers,it++,i);


         for(int i=0;i<transfers.size();i++)
            if(transfers.get(i).getStatus().getValue() == 1)
               Collections.swap(transfers,it++,i);

         toSendTransfers.addAll(transfers);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void loadToReceiveTransfers() {
      toReceiveTransfers.clear();
      try {
         List<BankTransfer> transfers = dbController.getToReceiveBankTransfers(currentUser.getId());
         int it = 0;
         for(int i=0;i<transfers.size();i++)
            if(transfers.get(i).getStatus().getValue() == 0)
               Collections.swap(transfers,it++,i);


         for(int i=0;i<transfers.size();i++)
            if(transfers.get(i).getStatus().getValue() == 1)
               Collections.swap(transfers,it++,i);

         toReceiveTransfers.addAll(transfers);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private class StatusImageCell extends TableCell<BankTransfer, ImageView> {
      ImageView imageView = new ImageView();

      public StatusImageCell() {
         imageView.setPreserveRatio(true);
         imageView.setFitHeight(20);
      }

      @Override
      protected void updateItem(ImageView item, boolean empty) {
         super.updateItem(item, empty);
         if (!empty) {
            BankTransfer transfer = (BankTransfer) StatusImageCell.this.getTableRow().getItem();
            if (transfer == null)
               return;
            int statusId = transfer.getStatus().getValue();
            String path = "/graphics/";
            switch (statusId) {
               case 0: path += "notpaid.png"; break;
               case 1: path += "waiting.png"; break;
               case 2: path += "paid.png"; break;
            }
            Image image = new Image(getClass().getResourceAsStream(path));
            imageView.setImage(image);
            setGraphic(imageView);
         }
         else
            setGraphic(null);
      }
   }

   private class UpdateStatusButtonCell extends TableCell<BankTransfer, Boolean> {
      final Button btnUpdateStatus = new Button();

      {
         btnUpdateStatus.setPrefSize(25, 25);
         btnUpdateStatus.setPadding(new Insets(0, 0, 0, 0));
      }

      boolean toSendTransfersTable;

      public UpdateStatusButtonCell(ImageView imageView, boolean toSend) {
         this.toSendTransfersTable = toSend;
         setPadding(new Insets(0, 0, 0, 0));
         btnUpdateStatus.setGraphic(imageView);
         btnUpdateStatus.setOnAction(event -> {
            try {
               BankTransfer transfer = (BankTransfer) UpdateStatusButtonCell.this.getTableRow().getItem();
               transfer.updateStatus(currentUser.getId());
               dbController.setBankTransfersStatus(transfer.getId(), transfer.getStatus().getValue());
               if (toSendTransfersTable)
                  loadToSendTransfers();
               else
                  loadToReceiveTransfers();
            }
            catch (Exception e) {
               e.printStackTrace();
            }
         });
      }

      @Override
      protected void updateItem(Boolean item, boolean empty) {
         super.updateItem(item, empty);
         if (!empty) {
            BankTransfer transfer = (BankTransfer) UpdateStatusButtonCell.this.getTableRow().getItem();
            if (transfer == null)
               return;
            int status = transfer.getStatus().getValue();

            if (toSendTransfersTable && status == 0 || !toSendTransfersTable && status != 2)
               btnUpdateStatus.setDisable(false);
            else
               btnUpdateStatus.setDisable(true);
            setGraphic(btnUpdateStatus);
         }
         else
            setGraphic(null);
      }
   }
}
