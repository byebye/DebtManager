package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetCreatorWindow extends Stage {

    public BudgetCreatorWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/BudgetCreatorWindow.fxml"));
        setTitle("DeptManager - Budget Creator");
        setScene(new Scene(root));
    }
}
