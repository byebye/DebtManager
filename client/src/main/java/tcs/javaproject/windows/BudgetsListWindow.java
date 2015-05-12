package tcs.javaproject.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tcs.javaproject.controllers.BudgetsListController;

import java.io.IOException;

public class BudgetsListWindow extends Stage {

    public BudgetsListWindow(String userEmail) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetsListWindow.fxml"));
        Parent root = fxmlLoader.load();
        BudgetsListController controller = fxmlLoader.<BudgetsListController>getController();
        controller.setUserEmail(userEmail);
        controller.fillBudgetsTable();
        setTitle("DeptManager - My Budgets");
        setScene(new Scene(root));
    }
}
