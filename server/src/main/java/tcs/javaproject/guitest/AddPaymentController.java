package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Vsmasster on 07.05.15.
 */


public class AddPaymentController implements Initializable {
    @FXML
    private Button btnAddPayment;
    @FXML
    private TextField txtFieldAmount;
    @FXML
    private TextArea txtAreaWhat;

    private Budget budget;

    public void setBudget(Budget budget){
        this.budget = budget;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAddPayment.setOnAction(event->{
            //add record to database, close window, refresh payments list
            Stage stage = (Stage)btnAddPayment.getScene().getWindow();
            stage.close();
        });
    }
}
