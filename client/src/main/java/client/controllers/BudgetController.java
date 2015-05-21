package client.controllers;

import client.windows.*;
import common.Budget;
import common.DBHandler;
import common.Payment;
import common.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetController implements Initializable {

   @FXML
   private Text txtBudgetName;
   @FXML
   private Text txtBudgetDescription;
   @FXML
   private Text txtSum, txtSumPerPerson;
   @FXML
   private Button btnAddPayment, btnSettle, btnAddParticipant, btnBudgetClose, btnBudgetDelete;
   @FXML
   private TableView<Payment> tabUnaccPayments, tabAccPayments;
   @FXML
   private TableView<User> tabParticipants;
   @FXML
   private TableColumn colUnaccWhat, colUnaccWho, colUnaccAmount, colConfirm;
   @FXML
   private TableColumn colAccWhat, colAccWho, colAccAmount;
   @FXML
   private TableColumn colUserName, colUserMail, colUserBalance;

   private static DBHandler dbController = LoginController.dbController;

   private Budget budget;
   private int userId;
   double spentMoneySum = 0;
   private BudgetWindow budgetWindow;
   private final ObservableList<User> participantsList = FXCollections.observableArrayList();
   private final ObservableList<Payment> accountedPayments = FXCollections.observableArrayList();
   private final ObservableList<Payment> unaccountedPayments = FXCollections.observableArrayList();

   public void setBudget(Budget budget, int userId, BudgetWindow budgetWindow) {
      this.budget = budget;
      txtBudgetName.setText(budget.getName());
      txtBudgetDescription.setText(budget.getDescription());
      this.userId = userId;
      this.budgetWindow = budgetWindow;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnSettle.setOnAction(event -> {
         try {
            ObservableList<Payment> paymentsToSettle = FXCollections.observableArrayList();
            for (Payment p : unaccountedPayments)
               if (p.getAccept())
                  paymentsToSettle.add(p);

            SettleWindow settleWindow = new SettleWindow(budget, paymentsToSettle, this);
            settleWindow.initModality(Modality.APPLICATION_MODAL);
            settleWindow.showAndWait();
            paymentsToSettle.clear();
         }
         catch(Exception e){
               e.printStackTrace();
         }
         //dbController.settleUnaccountedPayments(budget.getId());
         //fillTabUnaccPayments();
         //fillTabAccPayments();
      });

      btnAddPayment.setOnAction(event -> {
         try {
            AddPaymentWindow addPaymentWindow = new AddPaymentWindow(budget, userId);
            addPaymentWindow.setOnHidden(e -> fillTabUnaccPayments());
            addPaymentWindow.show();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      });

      btnAddParticipant.setOnAction(event -> {
         try {
            AddUserToBudgetWindow addUserToBudgetWindow = new AddUserToBudgetWindow(budgetWindow);
            addUserToBudgetWindow.setOnHidden(e -> fillTabUnaccPayments());
            addUserToBudgetWindow.show();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      });

      btnBudgetClose.setOnAction(event -> {
         Stage stage = (Stage) btnBudgetClose.getScene().getWindow();
         stage.close();
      });

      btnBudgetDelete.setOnAction(event -> {
         // TODO window to confirm deletion;
         try {
            dbController.deleteBudget(budget);
         }
         catch (RemoteException e) {
            e.printStackTrace();
         }
         Stage stage = (Stage) btnBudgetDelete.getScene().getWindow();
         stage.close();
      });

      //Table
      colUnaccWhat.setCellValueFactory(new PropertyValueFactory<Payment, String>("what"));
      colAccWhat.setCellValueFactory(new PropertyValueFactory<Payment, String>("what"));
      colUnaccWho.setCellValueFactory(new PropertyValueFactory<Payment, String>("who"));
      colAccWho.setCellValueFactory(new PropertyValueFactory<Payment, String>("who"));
      colUnaccAmount.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("amount"));
      colAccAmount.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("amount"));
      colUserName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
      colUserMail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
      colConfirm.setCellFactory(param -> new CheckBoxTableCell());
      colUserBalance.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, BigDecimal>, ObservableValue<BigDecimal>>() {
         public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<User, BigDecimal> p) {
            User participant = p.getValue();
            double balance = 0;
            if (participant != null)
                balance = participant.getSpentMoney() - spentMoneySum / participantsList.size();
            return new ReadOnlyObjectWrapper<>(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_FLOOR));
         }
      });
      tabParticipants.setItems(participantsList);
      tabParticipants.setRowFactory(param -> {
         TableRow<User> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               User participant = row.getItem();
               try {
                  ParticipantDetailsWindow participantWindow = new ParticipantDetailsWindow(budget, participant);
                  participantWindow.setOnHidden(event -> {
                     fillTabParticipants();
                     fillTabUnaccPayments();
                  });
                  participantWindow.show();
               }
               catch (IOException e) {
                  e.printStackTrace();
               }
            }
         });
         return row;
      });
      tabUnaccPayments.setItems(unaccountedPayments);
      tabAccPayments.setItems(accountedPayments);
      tabUnaccPayments.setRowFactory(param -> {
         TableRow<Payment> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               Payment payment = row.getItem();
               try {
                  PaymentWindow paymentWindow = new PaymentWindow(payment, participantsList);
                  paymentWindow.setOnHidden(event -> fillTabUnaccPayments());
                  paymentWindow.show();
               }
               catch (IOException e) {
                  e.printStackTrace();
               }
            }
         });
         return row;
      });
   }

   void addParticipants(List<User> users) {
      users.removeAll(participantsList);
      participantsList.addAll(users);
      try {
         dbController.addBudgetParticipants(budget.getId(), users);
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
   }

   public void fillAllTables() {
      fillTabParticipants();
      fillTabUnaccPayments();
      fillTabAccPayments();
   }

   void fillTabParticipants() {
      participantsList.clear();
      try {
         participantsList.addAll(dbController.getBudgetParticipants(budget.getId()));
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
   }

   void fillTabAccPayments() {
      accountedPayments.clear();
      try {
         accountedPayments.addAll(dbController.getAllPayments(budget.getId(), true));
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
   }

   void fillTabUnaccPayments() {
      unaccountedPayments.clear();
      try {
         unaccountedPayments.addAll(dbController.getAllPayments(budget.getId(), false));
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
      spentMoneySum = 0;
      for (User participant : participantsList)
         participant.setSpentMoney(0);
      for (Payment p : unaccountedPayments) {
         spentMoneySum += p.getAmount();
         FilteredList<User> filtered = participantsList.filtered(user -> user.getId() == p.getUserId());
         if (!filtered.isEmpty()) {
            User participant = filtered.get(0);
            participant.addSpentMoney(p.getAmount());
         }
      }
      refreshBalanceCells();
      txtSum.setText("SUM: " + spentMoneySum + "$");
      String perPerson = String.format("%.2f", spentMoneySum / participantsList.size());
      txtSumPerPerson.setText("Sum / Person: " + perPerson + "$");
   }

   private void refreshBalanceCells() {
      tabParticipants.getColumns().get(2).setVisible(false);
      tabParticipants.getColumns().get(2).setVisible(true);
   }

   public static class CheckBoxTableCell extends TableCell<Payment, Boolean> {

      private final CheckBox checkBox = new CheckBox();

      public CheckBoxTableCell() {
         setAlignment(Pos.CENTER);
         checkBox.setOnAction(event -> {
            Payment payment = (Payment)CheckBoxTableCell.this.getTableRow().getItem();
            payment.setAccept(!payment.getAccept());
         });
      }

      @Override
      public void updateItem(Boolean item, boolean empty) {
         super.updateItem(item, empty);
         if(!empty) {
            checkBox.setSelected(true);
            setGraphic(checkBox);
         }
         else
            setGraphic(null);
      }

   }
}
