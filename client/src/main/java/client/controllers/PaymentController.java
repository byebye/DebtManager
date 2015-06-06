package client.controllers;

import client.view.ErrorHighlighter;
import common.Budget;
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

   @FXML
   private Text txtPaymentName;
   @FXML
   private TextField txtFieldAmount;
   @FXML
   private ComboBox boxChooseWho;
   @FXML
   private TextArea txtAreaWhat;
   @FXML
   private Button btnUpdate, btnRemove;
   @FXML
   private Label errorLabel;

   private static DBHandler dbController = LoginController.dbController;
   private final User currentUser = LoginController.currentUser;
   private Payment payment;
   private Budget budget;
   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   public void setPayment(Payment payment) {
      this.payment = payment;
   }

   public void setBudget(Budget budget) {
      this.budget = budget;
   }

   public void setParticipantsList(ObservableList<User> participants) {
      boxChooseWho.setEditable(false);
      boxChooseWho.setItems(participants);
      User current = participants.filtered(user -> user.getId() == payment.getUserId()).get(0);
      boxChooseWho.setValue(current);
      if (!currentUser.equals(budget.getOwner()))
         boxChooseWho.setDisable(true);
   }

   public void setObjectsText() {
      txtPaymentName.setText(payment.getWhat());
      txtAreaWhat.setText(payment.getWhat());
      txtFieldAmount.setText(Double.toString(payment.getAmount()));
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      TextFormatter<String> onlyNumberFormatter = new TextFormatter<>(change -> {
         change.setText(change.getText().replaceAll("[^0-9.]", ""));
         return change;
      });
      txtFieldAmount.setTextFormatter(onlyNumberFormatter);

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
         errorLabel.setText("");
         ErrorHighlighter.unhighlitghtFields(txtFieldAmount);

         BigDecimal amount = getAmount();
         if (amount == null)
            return;
         User chosenUser = (User) boxChooseWho.getSelectionModel().getSelectedItem();
         final int who = chosenUser.getId();
         try {
            dbController.updatePayment(payment.getId(), who, amount, txtAreaWhat.getText());
            currentStage.close();
         }
         catch (RemoteException e) {
            errorLabel.setText("Server connection error");
            e.printStackTrace();
         }
      });

      btnRemove.setOnAction(event -> {
         try {
            dbController.deletePayment(payment.getId());
            currentStage.close();
         }
         catch (RemoteException e) {
            errorLabel.setText("Server connection error");
            e.printStackTrace();
         }
      });
   }

   private BigDecimal getAmount() {
      try {
         return BigDecimal.valueOf(Double.parseDouble(txtFieldAmount.getText()));
      }
      catch (NumberFormatException e) {
         errorLabel.setText("Invalid amount value");
         ErrorHighlighter.highlightInvalidFields(txtFieldAmount);
         return null;
      }
   }
}
