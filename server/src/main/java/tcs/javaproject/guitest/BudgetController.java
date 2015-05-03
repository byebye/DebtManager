package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class BudgetController implements Initializable {
   @FXML
   private Text txtBudgetName;
   @FXML
   private Text txtBudgetDescription;

   private Budget budget;

   void setBudget(Budget budget) {
      this.budget = budget;
      txtBudgetName.setText(budget.getName());
      txtBudgetDescription.setText(budget.getDescription());
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }
}
