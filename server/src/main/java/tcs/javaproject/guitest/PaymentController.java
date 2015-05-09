package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Vsmasster on 07.05.15.
 */
public class PaymentController implements Initializable{
    private Payment payment;
    @FXML
    private Text txtPaymentName;
    @FXML
    private TextField txtFieldWho, txtFieldAmount;
    @FXML
    private TextArea txtAreaWhat;
    @FXML
    private Button btnUpdate,btnRemove;

    public void setPayment(Payment payment){
        this.payment = payment;
    }

    public void setObjectsText(){
        txtPaymentName.setText(payment.getWhat());
        txtFieldWho.setText(payment.getWho());
        txtAreaWhat.setText(payment.getWhat());
        txtFieldAmount.setText(Double.toString(payment.getAmount()));
    }

    @Override //TODO: sql code to update and remove payments
    public void initialize(URL location, ResourceBundle resources) {
        btnUpdate.setOnAction(event->{

        });

        btnRemove.setOnAction(event->{

        });
    }
}
