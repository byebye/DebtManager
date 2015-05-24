package client.controllers;

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

   private static DBHandler dbController = LoginController.dbController;

   private Budget budget;
   private int userId;

   public void setBudget(Budget budget) {
      this.budget = budget;
   }

   public void setUser(int userId) {
      this.userId = userId;
   }

   public void setParticipantsList(ObservableList<User> participants) {
      boxChooseWho.setEditable(false);
      boxChooseWho.setItems(participants);
      User current = participants.filtered(user -> user.getId() == userId).get(0);
      boxChooseWho.setValue(current);
      if (budget.getOwner().getId() != LoginController.currentUser.getId())
         boxChooseWho.setDisable(true);
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

      btnAddPayment.setOnAction(event -> {
         BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(txtFieldAmount.getText()));
         try {
            User chosenUser = (User) boxChooseWho.getSelectionModel().getSelectedItem();
            final int who = chosenUser.getId();
            dbController.addPayment(budget, who, amount, txtAreaWhat.getText());
         }
         catch (RemoteException e) {
            e.printStackTrace();
         }
         Stage stage = (Stage) btnAddPayment.getScene().getWindow();
         stage.close();
      });
      btnCancel.setOnAction(event -> {
         Stage stage = (Stage) btnCancel.getScene().getWindow();
         stage.close();
      });
   }
}
