package client.controllers;

import client.windows.LoginWindow;
import common.DBHandler;
import common.Payment;
import common.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
   private Payment payment;
   @FXML
   private Text txtPaymentName;
   @FXML
   private TextField txtFieldAmount;
   @FXML
   public ComboBox boxChooseWho;
   @FXML
   private TextArea txtAreaWhat;
   @FXML
   private Button btnUpdate, btnRemove;

   private static DBHandler dbController = LoginController.dbController;

   public void setPayment(Payment payment) {
      this.payment = payment;
   }

   public void setParticipantsList(ObservableList<User> participants) {
      boxChooseWho.setEditable(false);
      boxChooseWho.setItems(participants);
      User current = participants.filtered(user -> user.getId() == payment.getUserId()).get(0);
      boxChooseWho.setValue(current);
   }

   public void setObjectsText() {
      txtPaymentName.setText(payment.getWhat());
      txtAreaWhat.setText(payment.getWhat());
      txtFieldAmount.setText(Double.toString(payment.getAmount()));
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      Callback<ListView<User>, ListCell<User>> cellFactory = param -> new ListCell<User>() {
         @Override
         protected void updateItem(User user, boolean empty) {
            super.updateItem(user, empty);
            if (user != null)
               setText(user.getName());
         }
      };
      boxChooseWho.setButtonCell(cellFactory.call(null));
      boxChooseWho.setCellFactory(cellFactory);
      
      btnUpdate.setOnAction(event -> {
         BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(txtFieldAmount.getText()));
         User chosenUser = (User) boxChooseWho.getSelectionModel().getSelectedItem();
         final int who = chosenUser.getId();
         try {
            dbController.updatePayment(payment.getId(), who,
                                       amount,
                                       txtAreaWhat.getText());
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();
         }
         catch (RemoteException e) {
            e.printStackTrace();
         }
      });

      btnRemove.setOnAction(event -> {
         try {
            dbController.deletePayment(payment.getId());
            Stage stage = (Stage) btnRemove.getScene().getWindow();
            stage.close();
         }
         catch (RemoteException e) {
            e.printStackTrace();
         }
      });
   }
}
