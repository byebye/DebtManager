package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
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
   private Button btnAddPayment, btnSettle, btnAddParticipant, btnBudgetClose;
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

   void setBudget(Budget budget, int userId, BudgetWindow budgetWindow) {
      this.budget = budget;
      txtBudgetName.setText(budget.getName());
      txtBudgetDescription.setText(budget.getDescription());
      this.userId = userId;
      this.budgetWindow = budgetWindow;
   }

   void fillTabParticipants() {
      List<User> participantsList = dbController.getBudgetParticipants(budget.getId());
      tabParticipants.setItems(FXCollections.observableArrayList(participantsList));
   }

   void fillTabAccPayments() {
      List<Payment> accPaymentsList = dbController.getAllPayments(budget.getId(), true);
      if (accPaymentsList == null)
         return;
      tabAccPayments.setRowFactory(param -> {
         TableRow<Payment> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               Payment payment = row.getItem();
               try {
                  PaymentWindow paymentWindow = new PaymentWindow(payment);
                  paymentWindow.show();
               }
               catch (IOException e) {
                  e.printStackTrace();
               }
            }
         });
         return row;
      });
      tabAccPayments.setItems(FXCollections.observableArrayList(accPaymentsList));
   }

   void fillTabUnaccPayments() {
      List<Payment> unaccPaymentsList = dbController.getAllPayments(budget.getId(), false);
      if (unaccPaymentsList == null) {
         return;
      }
      double sum = 0;
      for (Payment p : unaccPaymentsList)
         sum += p.getAmount();
      tabUnaccPayments.setRowFactory(param -> {
         TableRow<Payment> row = new TableRow<>();
         row.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
               Payment payment = row.getItem();
               try {
                  PaymentWindow paymentWindow = new PaymentWindow(payment);
                  paymentWindow.show();
               }
               catch (IOException e) {
                  e.printStackTrace();
               }
            }
         });
         return row;
      });
      txtSum.setText("SUM: " + sum + "$");

      tabUnaccPayments.setItems(FXCollections.observableArrayList(unaccPaymentsList));
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnSettle.setOnAction(event -> {
         dbController.settleUnaccountedPayments(budget.getId());
         fillTabAccPayments();
         fillTabUnaccPayments();
      });

      btnAddPayment.setOnAction(event -> {
         try {
            AddPaymentWindow addPaymentWindow = new AddPaymentWindow(budget, userId, budgetWindow);
            addPaymentWindow.show();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      });

      btnAddParticipant.setOnAction(event -> {
         try {
            AddUserToBudgetWindow addUserToBudgetWindow = new AddUserToBudgetWindow(budget.getId(), budgetWindow);
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

      //Table
      colUnaccWhat.setCellValueFactory(new PropertyValueFactory<Payment, String>("what"));
      colAccWhat.setCellValueFactory(new PropertyValueFactory<Payment, String>("what"));
      colUnaccWho.setCellValueFactory(new PropertyValueFactory<Payment, String>("who"));
      colAccWho.setCellValueFactory(new PropertyValueFactory<Payment, String>("who"));
      colUnaccAmount.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("amount"));
      colAccAmount.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("amount"));
      colUserName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
      colUserMail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
   }
}
