package client.controllers;

import client.view.ErrorHighlighter;
import common.Budget;
import common.DBHandler;
import common.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class AddPaymentController implements Initializable {

   @FXML
   private Button btnAddPayment, btnCancel;
   @FXML
   public ComboBox boxChooseWho;
   @FXML
   private TextField txtFieldAmount;
   @FXML
   private TextArea txtAreaWhat;
   @FXML
   private Label errorLabel;

   private static DBHandler dbController = LoginController.dbController;

   private final User currentUser = LoginController.currentUser;
   private Budget budget;
   private Stage currentStage;

   public void setStage(Stage stage) {
      currentStage = stage;
   }

   public void setBudget(Budget budget) {
      this.budget = budget;
   }

   public void setParticipantsList(ObservableList<User> participants) {
      boxChooseWho.setEditable(false);
      boxChooseWho.setItems(participants);
      final User owner = participants.filtered(user -> currentUser.equals(user)).get(0);
      boxChooseWho.setValue(owner);
      if (!currentUser.equals(budget.getOwner()))
         boxChooseWho.setDisable(true);
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

      btnAddPayment.setOnAction(event -> {
         errorLabel.setText("");
         ErrorHighlighter.unhighlitghtFields(txtFieldAmount);

         BigDecimal amount = getAmount();
         if (amount == null)
            return;
         try {
            User chosenUser = (User) boxChooseWho.getSelectionModel().getSelectedItem();
            final int who = chosenUser.getId();
            dbController.addPayment(budget, who, amount, txtAreaWhat.getText());
            currentStage.close();
         }
         catch (RemoteException e) {
            errorLabel.setText("Server connection error");
            e.printStackTrace();
         }
      });

      btnCancel.setOnAction(event -> currentStage.close());
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
