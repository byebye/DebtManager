package tcs.javaproject.guitest;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import tcs.javaproject.database.tables.Users;

import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
            addRecord();
            Stage stage = (Stage)btnAddPayment.getScene().getWindow();
            stage.close();
        });
    }

    void addRecord(){

    }
}
