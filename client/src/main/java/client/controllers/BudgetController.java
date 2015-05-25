package client.controllers;

import client.windows.*;
import common.*;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
   private TableView<Settlement> tabSettleHistory;
   @FXML
   private TableColumn colDate,colAmount,colStatus;
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
   private final ObservableList<Settlement> settleHistory = FXCollections.observableArrayList();

   public void setBudget(Budget budget, int userId, BudgetWindow budgetWindow) {
      this.budget = budget;
      txtBudgetName.setText(budget.getName());
      txtBudgetDescription.setText(budget.getDescription());
      this.userId = userId;
      this.budgetWindow = budgetWindow;
      if (budget.getOwner().getId() != LoginController.currentUser.getId()) {
         btnAddParticipant.setDisable(true);
         btnSettle.setDisable(true);
         btnBudgetDelete.setDisable(true);
      }
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
            settleWindow.initOwner(btnSettle.getScene().getWindow());
            settleWindow.showAndWait();
            paymentsToSettle.clear();
            settleHistory.clear();
            fillTabSettleHistory();
         }
         catch(Exception e){
               e.printStackTrace();
         }
      });

      btnAddPayment.setOnAction(event -> {
         try {
            AddPaymentWindow addPaymentWindow = new AddPaymentWindow(budget, userId, participantsList);
            addPaymentWindow.initOwner(btnAddPayment.getScene().getWindow());
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
            addUserToBudgetWindow.initOwner(btnAddParticipant.getScene().getWindow());
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
         Alert userCreatedAlert = new Alert(Alert.AlertType.CONFIRMATION);
         userCreatedAlert.setTitle("Confirm deletion");
         userCreatedAlert.setHeaderText("Are you sure you want to delete this budget?");
         userCreatedAlert.setContentText("This operation cannot be undone.");
         Optional<ButtonType> result = userCreatedAlert.showAndWait();
         if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
               try {
                  dbController.deleteBudget(budget);
               }
               catch (RemoteException e) {
                  e.printStackTrace();
               }
               Stage stage = (Stage) btnBudgetDelete.getScene().getWindow();
               stage.close();
            }
            catch (Exception e) {
               e.printStackTrace();
            }
         }
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
      colDate.setCellValueFactory(new PropertyValueFactory<Settlement,String>("date"));
      colAmount.setCellValueFactory(new PropertyValueFactory<Settlement,Double>("amount"));
      colStatus.setCellValueFactory(new PropertyValueFactory<Settlement,String>("status"));
      colUserBalance.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, BigDecimal>, ObservableValue<BigDecimal>>() {
         public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<User, BigDecimal> p) {
            User participant = p.getValue();
            double balance = 0;
            if (participant != null)
                balance = participant.getSpentMoney() - spentMoneySum / participantsList.size();
            return new ReadOnlyObjectWrapper<>(new BigDecimal(balance).setScale(2, BigDecimal.ROUND_FLOOR));
         }
      });
      tabSettleHistory.setItems(settleHistory);
      tabParticipants.setItems(participantsList);
      tabParticipants.setRowFactory(param -> {
         TableRow<User> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               User participant = row.getItem();
               try {
                  ParticipantDetailsWindow participantWindow = new ParticipantDetailsWindow(budget, participant);
                  participantWindow.initOwner(btnAddParticipant.getScene().getWindow());
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
            if (row.isEmpty())
               return;
            Payment payment = row.getItem();
            if (budget.getOwner().getId() == LoginController.currentUser.getId()
                || payment.getUserId() == LoginController.currentUser.getId()) {
               if (mouseEvent.getClickCount() == 2) {
                  try {
                     PaymentWindow paymentWindow = new PaymentWindow(budget, payment, participantsList);
                     paymentWindow.initOwner(btnAddParticipant.getScene().getWindow());
                     paymentWindow.setOnHidden(event -> fillTabUnaccPayments());
                     paymentWindow.show();
                  }
                  catch (IOException e) {
                     e.printStackTrace();
                  }
               }
            }
            // TODO else: you have no rights to edit this payment (special color or information)

         });
         return row;
      });
   }

   void addParticipants(List<User> users) {
      users.removeAll(participantsList);
      participantsList.addAll(users);
      try {
         List<User> usersSerializable = new ArrayList<>(users);
         dbController.addBudgetParticipants(budget.getId(), usersSerializable);
      }
      catch (RemoteException e) {
         e.printStackTrace();
      }
   }

   public void fillAllTables() {
      fillTabParticipants();
      fillTabUnaccPayments();
      fillTabAccPayments();
      fillTabSettleHistory();
   }

   void fillTabSettleHistory(){
      try {
         settleHistory.addAll(dbController.getAllSettlements(budget.getId()));
      }catch(Exception e){
         System.out.println("Access denied");
         e.printStackTrace();
      }
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

   public class CheckBoxTableCell extends TableCell<Payment, Boolean> {

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
