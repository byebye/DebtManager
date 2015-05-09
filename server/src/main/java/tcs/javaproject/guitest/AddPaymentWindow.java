package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPaymentWindow extends Stage{
    public AddPaymentWindow(Budget budget) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddPaymentWindow.fxml"));
        Parent root = fxmlLoader.load();
        AddPaymentController controller = fxmlLoader.<AddPaymentController>getController();
        controller.setBudget(budget);
        setTitle("DeptManager - " + budget.getName());
        setScene(new Scene(root));
    }
}
