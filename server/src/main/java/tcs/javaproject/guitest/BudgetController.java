package tcs.javaproject.guitest;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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
import org.jooq.*;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Budgets;
import tcs.javaproject.database.tables.Payments;
import tcs.javaproject.database.tables.UserBudget;
import tcs.javaproject.database.tables.Users;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import static org.jooq.impl.DSL.count;

public class BudgetController implements Initializable {
   @FXML
   private Text txtBudgetName;
   @FXML
   private Text txtBudgetDescription;
   @FXML
   private Text txtSum;
   @FXML
   private Button btnAddPayment, btnSettle;
   @FXML
   private TableView<Payment> tabUnaccPayments;
   @FXML
   private TableView<User> tabParticipants;
   @FXML
   private TableColumn colWhat,colWho,colAmount,colUserName,colUserMail;



   private Budget budget;
   private int userId;
   private BudgetWindow budgetWindow;

   void setBudget(Budget budget,int userId,BudgetWindow budgetWindow) {
      this.budget = budget;
      txtBudgetName.setText(budget.getName());
      txtBudgetDescription.setText(budget.getDescription());
      this.userId = userId;
      this.budgetWindow = budgetWindow;
   }

   void fillTabUnaccPayments(){
      double sum = 0;
      List<Payment> unaccPaymentsList = getUnaccPayments();
      if(unaccPaymentsList != null) {
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
                  } catch (IOException e) {
                     e.printStackTrace();
                  }
               }
            });
            return row;
         });
      }
      txtSum.setText("SUM: "+sum+"$");

      tabUnaccPayments.setItems(FXCollections.observableArrayList(unaccPaymentsList));
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnSettle.setOnAction(event->{

      });

      btnAddPayment.setOnAction(event -> {
         try {
            AddPaymentWindow addPaymentWindow = new AddPaymentWindow(budget,userId,budgetWindow);
            addPaymentWindow.show();
         } catch (IOException e) {
            e.printStackTrace();
         }
      });

      //Table
      colWhat.setCellValueFactory(new PropertyValueFactory<Payment, String>("what"));
      colWho.setCellValueFactory(new PropertyValueFactory<Payment,String>("who"));
      colAmount.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("amount"));

      colUserName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
      colUserMail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

      final ObservableList<User> dataParticipants = FXCollections.observableArrayList(
              new User(1,"John","john@example.com"),
              new User(2,"Marry","marry@example.com")
      );

      int sum = 0;



      tabParticipants.setItems(dataParticipants);
   }

   List<Payment> getUnaccPayments(){
      List<Payment> payments = new LinkedList<>();
      String url = "jdbc:postgresql://localhost/debtmanager";

      try (Connection conn = DriverManager.getConnection(url, "debtmanager", "debtmanager")) {
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);

         Result<Record4<Integer, Integer, String, BigDecimal>> result = create
               .select(Payments.PAYMENTS.ID, Payments.PAYMENTS.USER_ID, Payments.PAYMENTS.DESCRIPTION,Payments.PAYMENTS.AMOUNT)
               .from(Payments.PAYMENTS)
               .where(Payments.PAYMENTS.BUDGET_ID.equal(budget.getId())).fetch();

         for (Record4<Integer, Integer, String, BigDecimal> payment : result) {
            int user_id = payment.value2();
            Result<Record1<String>> userName = create
                  .select(Users.USERS.NAME)
                  .from(Users.USERS)
                  .where(Users.USERS.ID.equal(user_id))
                  .fetch();
            payments.add(new Payment(userName.get(0).value1(), payment.value3(), payment.value4().doubleValue(), payment.value1()));
         }
         return payments;
      }
      catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
}
