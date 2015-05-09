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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
   private TableColumn colWhat, colWho, colAmount, colUserName, colUserMail;

   private Budget budget;

   void setBudget(Budget budget) {
      this.budget = budget;
      txtBudgetName.setText(budget.getName());
      txtBudgetDescription.setText(budget.getDescription());
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnSettle.setOnAction(event -> {

      });

      btnAddPayment.setOnAction(event -> {
         try {
            AddPaymentWindow addPaymentWindow = new AddPaymentWindow(budget);
            addPaymentWindow.show();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      });

      //Table
      colWhat.setCellValueFactory(new PropertyValueFactory<Payment, String>("what"));
      colWho.setCellValueFactory(new PropertyValueFactory<Payment, String>("who"));
      colAmount.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("amount"));

      colUserName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
      colUserMail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

      final ObservableList<Payment> dataUnaccPayments = FXCollections.observableArrayList(
                                                                                                 new Payment("John",
                                                                                                             "Drinks",
                                                                                                             10),
                                                                                                 new Payment("Marry",
                                                                                                             "Food", 20)
      );

      final ObservableList<User> dataParticipants = FXCollections.observableArrayList(
                                                                                             new User(1, "John",
                                                                                                      "john@example.com"),
                                                                                             new User(2, "Marry",
                                                                                                      "marry@example.com")
      );

      int sum = 0;
      for (Payment p : dataUnaccPayments)
         sum += p.getAmount();

      tabUnaccPayments.setItems(dataUnaccPayments);
      tabParticipants.setItems(dataParticipants);

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
   }
}
