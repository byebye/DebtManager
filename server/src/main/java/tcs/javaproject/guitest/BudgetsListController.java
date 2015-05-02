package tcs.javaproject.guitest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class BudgetsListController implements Initializable {

    @FXML
    private Button btnLogout;
    @FXML
    private Button btnCreateNewBudget;
    @FXML
    private TableColumn colName, colCom;
    @FXML
    private TableColumn colPeop;
    @FXML
    private TableView<Budget> tabMyBudgets;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        /*
                               Buttons
        */
        btnLogout.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("You will be logged out soon!");
            }
        });

        btnCreateNewBudget.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Budget Creator not ready yet!");
            }
        });

        /*
                               Table
        */

        final ObservableList<Budget> data = FXCollections.observableArrayList(
                new Budget("Party at Jack", "Money for food and drinks", 6),
                new Budget("Gift for Czesiek", "20th birthday", 4)
        );

        colName.setCellValueFactory(
                new PropertyValueFactory<Budget, String>("name")
        );
        colCom.setCellValueFactory(
                new PropertyValueFactory<Budget, String>("description")
        );
        colPeop.setCellValueFactory(
                new PropertyValueFactory<Budget,Integer>("partNum")
        );

        tabMyBudgets.setItems(data);
    }
}
