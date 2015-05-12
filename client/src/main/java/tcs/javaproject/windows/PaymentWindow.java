package tcs.javaproject.windows;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tcs.javaproject.Payment;
import tcs.javaproject.User;
import tcs.javaproject.controllers.PaymentController;

import java.io.IOException;

public class PaymentWindow extends Stage {
    public PaymentWindow(Payment payment, ObservableList<User> participants) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PaymentWindow.fxml"));
        Parent root = fxmlLoader.load();
        PaymentController controller = fxmlLoader.<PaymentController>getController();
        controller.setPayment(payment);
        controller.setParticipantsList(participants);
        controller.setObjectsText();
        setTitle("DeptManager - show payment details");
        setScene(new Scene(root));
    }
}
