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

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class BudgetController implements Initializable {
   @FXML
   private Text txtBudgetName;
   @FXML
   private Text txtBudgetDescription;
   @FXML
   private Text txtSum;
   @FXML
   private Button btnAddPayment;
   @FXML
   private TableView<Payment> tabUnaccPayments;
   @FXML
   private TableColumn colWhat,colWho;
   @FXML
   private TableColumn colAmount;

   private Budget budget;

   void setBudget(Budget budget) {
      this.budget = budget;
      txtBudgetName.setText(budget.getName());
      txtBudgetDescription.setText(budget.getDescription());
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      //Buttons
      btnAddPayment.setOnAction(event -> {
         try {
            AddPaymentWindow addPaymentWindow = new AddPaymentWindow(budget);
            addPaymentWindow.show();
         } catch (IOException e) {
            e.printStackTrace();
         }
      });

      //Table
      colWhat.setCellValueFactory(new PropertyValueFactory<Payment, String>("what"));
      colWho.setCellValueFactory(new PropertyValueFactory<Payment,String>("who"));
      colAmount.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("amount"));

      final ObservableList<Payment> list = FXCollections.observableArrayList(
              new Payment("John","Drinks",10),
              new Payment("Marry","Food",20)
      );
      int sum = 0;
      for(Payment p: list)
         sum+=p.getAmount();

      tabUnaccPayments.setItems(list);

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

      txtSum.setText("SUM: "+sum+"$");
   }
}
