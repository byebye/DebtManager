package tcs.javaproject.guitest;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import tcs.javaproject.database.DatabaseController;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Vsmasster on 12.05.15.
 */
public class SettleController implements Initializable {
   @FXML
   TableView<BankTransfer> tabSettleView;
   @FXML
   TableColumn colWho,colWhom,colAmount,colAccNumber,colConfirm;
   @FXML
   Button btnConfirm, btnDecline;

   private final DatabaseController dbController = LoginWindow.dbController;
   private static ObservableList<BankTransfer> content = FXCollections.observableArrayList();
   private Budget budget;
   private BudgetController parentController;

   public void setBudget(Budget budget,BudgetController parentController){
      this.budget = budget;
      this.parentController = parentController;
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
      colConfirm.setCellFactory(param -> new CheckBoxTableCell());

      btnConfirm.setOnAction(event -> {
         dbController.settleUnaccountedPayments(budget.getId(), content);
         parentController.fillAllTables();
         content.clear();
         Stage stage = (Stage) btnConfirm.getScene().getWindow();
         stage.close();
      });

      btnDecline.setOnAction(event->{
         for(BankTransfer b: content)
            b.setAccept(true);
         
         content.clear();
         Stage stage = (Stage)btnDecline.getScene().getWindow();
         stage.close();
      });
   }

   public static class CheckBoxTableCell extends TableCell<BankTransfer, Boolean> {

      private final CheckBox checkBox;

      public CheckBoxTableCell() {
         this.checkBox = new CheckBox();
         this.checkBox.setAlignment(Pos.CENTER);

         setAlignment(Pos.CENTER);
         setGraphic(checkBox);
         checkBox.setSelected(true);
         checkBox.setOnAction(event->{
            BankTransfer bankTransfer = (BankTransfer)CheckBoxTableCell.this.getTableRow().getItem();
            bankTransfer.setAccept(!bankTransfer.getAccept());
         });
      }



      @Override public void updateItem(Boolean item, boolean empty) {
         super.updateItem(item, empty);

         if(empty)
            setGraphic(null);

      }

   }

}
