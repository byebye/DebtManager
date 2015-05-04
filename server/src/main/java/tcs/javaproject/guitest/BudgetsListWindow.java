package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetsListWindow extends Stage {

    public BudgetsListWindow(String username) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetsListWindow.fxml"));
        Parent root = fxmlLoader.load();
        BudgetsListController controller = fxmlLoader.<BudgetsListController>getController();
        controller.setUserName(username);
        controller.fillBudgetsTable();
        setTitle("DeptManager - My Budgets");
        setScene(new Scene(root));
    }
}
