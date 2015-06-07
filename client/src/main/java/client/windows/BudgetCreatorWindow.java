package client.windows;

import client.controllers.BudgetCreatorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetCreatorWindow extends Stage {

    public BudgetCreatorWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetCreatorWindow.fxml"));
        Parent root = fxmlLoader.load();
        BudgetCreatorController controller = fxmlLoader.<BudgetCreatorController>getController();

        controller.setStage(this);

        setTitle("DeptManager - Budget Creator");
        setScene(new Scene(root));
        initModality(Modality.APPLICATION_MODAL);
    }
}
