package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tcs.javaproject.database.DatabaseController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetController implements Initializable {
   @FXML
   private Text txtBudgetName;
   @FXML
   private Text txtBudgetDescription;
   @FXML
   private Text txtSum;
   @FXML
   private Button btnAddPayment, btnSettle, btnAddParticipant, btnBudgetClose, btnBudgetDelete;
   @FXML
   private TableView<Payment> tabUnaccPayments, tabAccPayments;
   @FXML
   private TableView<User> tabParticipants;
   @FXML
   private TableColumn colUnaccWhat, colUnaccWho, colUnaccAmount, colAccWhat, colAccWho, colAccAmount, colUserName, colUserMail;

   private final DatabaseController dbController = LoginWindow.dbController;

   private Budget budget;
   private int userId;
   private BudgetWindow budgetWindow;
   private final ObservableList<User> participantsList = FXCollections.observableArrayList();
   private final ObservableList<Payment> accountedPayments = FXCollections.observableArrayList();
   private final ObservableList<Payment> unaccountedPayments = FXCollections.observableArrayList();

   void setBudget(Budget budget, int userId, BudgetWindow budgetWindow) {
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
         dbController.settleUnaccountedPayments(budget.getId());
         fillTabUnaccPayments();
         fillTabAccPayments();
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
         dbController.deleteBudget(budget);
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
      tabParticipants.setItems(participantsList);
      tabParticipants.setRowFactory(param -> {
         TableRow<User> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               User participant = row.getItem();
               try {
                  ParticipantDetailsWindow participantWindow = new ParticipantDetailsWindow(budget, participant);
                  participantWindow.setOnHidden(event -> fillTabParticipants());
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
      //      TODO discuss if accounted payments should be editable
      //      tabAccPayments.setRowFactory(param -> {
      //         TableRow<Payment> row = new TableRow<>();
      //         row.setOnMouseClicked(mouseEvent -> {
      //            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
      //               Payment payment = row.getItem();
      //               try {
      //                  PaymentWindow paymentWindow = new PaymentWindow(payment);
      //                  paymentWindow.show();
      //               }
      //               catch (IOException e) {
      //                  e.printStackTrace();
      //               }
      //            }
      //         });
      //         return row;
      //      });
   }

   void addParticipants(List<User> users) {
      users.removeAll(participantsList);
      participantsList.addAll(users);
      dbController.addBudgetParticipants(budget.getId(), users);
   }

   void fillAllTables() {
      fillTabUnaccPayments();
      fillTabAccPayments();
      fillTabParticipants();
   }

   void fillTabParticipants() {
      participantsList.clear();
      participantsList.addAll(dbController.getBudgetParticipants(budget.getId()));
   }

   void fillTabAccPayments() {
      accountedPayments.clear();
      accountedPayments.addAll(dbController.getAllPayments(budget.getId(), true));
   }

   void fillTabUnaccPayments() {
      unaccountedPayments.clear();
      unaccountedPayments.addAll(dbController.getAllPayments(budget.getId(), false));
      double sum = 0;
      for (Payment p : unaccountedPayments)
         sum += p.getAmount();
      txtSum.setText("SUM: " + sum + "$");
   }
}
