package client.windows;

import client.controllers.BudgetsListController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetsListWindow extends Stage {

    public BudgetsListWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/BudgetsListWindow.fxml"));
        Parent root = fxmlLoader.load();
        BudgetsListController controller = fxmlLoader.<BudgetsListController>getController();

        controller.setStage(this);
        //happens in setStage
        //controller.update();

        setTitle("DeptManager - My Budgets");
        setScene(new Scene(root));
        initModality(Modality.APPLICATION_MODAL);
    }
}
