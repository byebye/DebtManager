package tcs.javaproject.guitest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Vsmasster on 07.05.15.
 */
public class PaymentWindow extends Stage {
    public PaymentWindow(Payment payment) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PaymentWindow.fxml"));
        Parent root = fxmlLoader.load();
        PaymentController controller = fxmlLoader.<PaymentController>getController();
        controller.setPayment(payment);
        controller.setObjectsText();
        setTitle("DeptManager - show payment details");
        setScene(new Scene(root));
    }
}
