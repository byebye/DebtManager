package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tcs.javaproject.database.DatabaseController;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Vsmasster on 12.05.15.
 */
public class SettleController implements Initializable {
   @FXML
   TableView tabSettleView;
   @FXML
   TableColumn colWho,colWhom,colAmount,colAccNumber;
   @FXML
   Button btnSettle, btnDeciline;

   private final DatabaseController dbController = LoginWindow.dbController;
   private final ObservableList<BankTransfer> content = FXCollections.observableArrayList();
   private Budget budget;

   public void setBudget(Budget budget){
      this.budget = budget;
   }

   public void fillAllTables(){
      content.addAll(dbController.calculateBankTransfers(budget.getId()));
      tabSettleView.setItems(content);
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      colWho.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("who"));
      colWhom.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("whom"));
      colAmount.setCellValueFactory(new PropertyValueFactory<BankTransfer, BigDecimal>("amount"));
      colAccNumber.setCellValueFactory(new PropertyValueFactory<BankTransfer, String>("bankAccount"));
      btnSettle.setOnAction(event -> {
         dbController.settleUnaccountedPayments(budget.getId());
         Stage stage = (Stage) btnSettle.getScene().getWindow();
         stage.close();
      });

      btnDeciline.setOnAction(event->{
         Stage stage = (Stage)btnDeciline.getScene().getWindow();
         stage.close();
      });
   }
}
