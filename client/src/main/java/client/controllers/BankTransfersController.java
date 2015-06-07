package client.controllers;

//import com.sun.xml.internal.ws.api.message.Packet;

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
import java.util.ResourceBundle;

/**
 * Created by vsmasster on 26.05.15.
 */
public class BankTransfersController implements Initializable {
   @FXML
   MenuButton menuChooseTransferType;
   @FXML
   MenuItem itemMyTransfers, itemOthersTransfers;
   @FXML
   Text txtTitle;
   @FXML
   TableView tabBankTransfers;
   @FXML
   TableColumn colBudgetName, colWho, colAmount, colAction, colStatus;
   @FXML
   Button btnClose;

   private static DBHandler dbController = LoginController.dbController;
   private final ObservableList<BankTransfer> contentList = FXCollections.observableArrayList();
   private boolean ifMyTransfersActive = false;
   private final User currentUser = LoginController.currentUser;

   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnClose.setOnAction(event -> currentStage.close());

      //MenuItem
      itemMyTransfers.setOnAction(event -> {
         loadMyTransfers();
         ifMyTransfersActive = true;
         txtTitle.setText("My transfers");
         tabBankTransfers.setVisible(true);
      });

      itemOthersTransfers.setOnAction(event -> {
         loadOthersTransfers();
         ifMyTransfersActive = false;
         txtTitle.setText("Others transfers");
         tabBankTransfers.setVisible(true);
      });

      //Table
      colBudgetName.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("budgetName"));
      colWho.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("whoAcc"));
      colAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer, BigDecimal>("amount"));
      colStatus.setCellFactory(param -> new StatusImageCell());
      colAction.setCellFactory(param -> new UpdateStatusButtonCell());
      tabBankTransfers.setItems(contentList);
   }

   public void loadMyTransfers() {
      contentList.clear();
      try {
         contentList.addAll(dbController.getMyBankTransfers(currentUser.getId()));
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void loadOthersTransfers() {
      contentList.clear();
      try {
         contentList.addAll(dbController.getOthersBankTransfers(currentUser.getId()));
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
            int statusId = transfer.getStatus().getValue();
            String path = "/graphics/";
            switch(statusId) {
               case 0: path += "notpaid.png"; break;
               case 1: path += "waiting.png"; break;
               case 2: path += "paid.png"; break;
            }

            Image image = new Image(getClass().getClass().getResourceAsStream(path));
            imageView.setImage(image);
            setGraphic(imageView);
         }
         else
            setGraphic(null);
      }
   }

   private class UpdateStatusButtonCell extends TableCell<BankTransfer, Button> {
      final Button btnUpdateStatus = new Button();
      ImageView payImageView = new ImageView();
      ImageView confirmImageView = new ImageView();

      public UpdateStatusButtonCell() {
         setPadding(new Insets(0, 0, 0, 0));
         Image image1 = new Image(getClass().getClass().getResourceAsStream("/graphics/pay.png"));
         payImageView.setImage(image1);
         payImageView.setPreserveRatio(true);
         payImageView.setFitHeight(15);
         Image image2 = new Image(getClass().getClass().getResourceAsStream("/graphics/ok.png"));
         confirmImageView.setImage(image2);
         confirmImageView.setPreserveRatio(true);
         confirmImageView.setFitHeight(15);
         btnUpdateStatus.setPrefSize(25, 25);
         btnUpdateStatus.setPadding(new Insets(0, 0, 0, 0));
         btnUpdateStatus.setOnAction(event -> {
            try {
//               dbController.updateBankTransferStatus(((BankTransfer) UpdateStatusButtonCell.this.getTableRow().getItem()).getId(),
//                                                     currentUser.getId());
               if (ifMyTransfersActive)
                  loadMyTransfers();
               else loadOthersTransfers();

            }
            catch (Exception e) {
               e.printStackTrace();
            }
         });
      }

      @Override
      protected void updateItem(Button item, boolean empty) {
         super.updateItem(item, empty);
         if (!empty && item != null) {
            BankTransfer transfer = (BankTransfer) UpdateStatusButtonCell.this.getTableRow().getItem();
            int statusId = transfer.getStatus().getValue();
            if (!ifMyTransfersActive && statusId != 2) {
               btnUpdateStatus.setGraphic(confirmImageView);
               setGraphic(btnUpdateStatus);
            }
            else if (ifMyTransfersActive && statusId == 0) {
               btnUpdateStatus.setGraphic(payImageView);
               setGraphic(btnUpdateStatus);
            }
            else setGraphic(null);
         }
         else
            setGraphic(null);
      }
   }
}
