package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetsListWindow extends Stage {

    public BudgetsListWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/BudgetsListWindow.fxml"));
        setTitle("DeptManager - My Budgets");
        setScene(new Scene(root));
    }
}
