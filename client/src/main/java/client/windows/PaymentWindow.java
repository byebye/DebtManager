package client.windows;

import client.controllers.PaymentController;
import common.Budget;
import common.Payment;
import common.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PaymentWindow extends Stage {
    public PaymentWindow(Budget budget, Payment payment, ObservableList<User> participants) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PaymentWindow.fxml"));
        Parent root = fxmlLoader.load();
        PaymentController controller = fxmlLoader.<PaymentController>getController();
        controller.setBudget(budget);
        controller.setPayment(payment);
        controller.setParticipantsList(participants);
        controller.setObjectsText();
        setTitle("DeptManager - show payment details");
        setScene(new Scene(root));
        initModality(Modality.WINDOW_MODAL);
    }
}
