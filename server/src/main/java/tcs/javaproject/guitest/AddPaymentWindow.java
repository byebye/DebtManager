package tcs.javaproject.guitest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Created by Vsmasster on 07.05.15.
 */
public class AddPaymentWindow extends Stage{

   AddPaymentController controller;
   public AddPaymentController getController(){
      return controller;
   }
    public AddPaymentWindow(Budget budget,int userId,BudgetWindow parent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddPaymentWindow.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.<AddPaymentController>getController();
        controller.setBudget(budget);
        controller.setUser(userId);
         controller.setParent(parent);
        setTitle("DeptManager - " + budget.getName());
        setScene(new Scene(root));
    }
}
