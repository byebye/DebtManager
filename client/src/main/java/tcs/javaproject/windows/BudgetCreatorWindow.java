package tcs.javaproject.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tcs.javaproject.controllers.BudgetCreatorController;

import java.io.IOException;

public class BudgetCreatorWindow extends Stage {

    public BudgetCreatorWindow(int userId) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetCreatorWindow.fxml"));
        Parent root = fxmlLoader.load();
        BudgetCreatorController controller = fxmlLoader.<BudgetCreatorController>getController();
        controller.setUserId(userId);

        setTitle("DeptManager - Budget Creator");
        setScene(new Scene(root));
    }
}
