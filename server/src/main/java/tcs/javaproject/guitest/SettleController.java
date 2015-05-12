package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
   TableColumn colWho,colWhom,colAmount;

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
   }
}
