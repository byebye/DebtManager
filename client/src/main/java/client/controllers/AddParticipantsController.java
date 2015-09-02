package client.controllers;

import client.utils.DataUtils;
import client.view.ErrorHighlighter;
import common.connection.DbHandler;
import common.data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddParticipantsController extends BasicController implements Initializable {

  @FXML
  private TextField fieldEmail;
  @FXML
  private Button buttonAddParticipant, buttonConfirm, buttonCancel;
  @FXML
  public TableColumn<User, String> columnParticipantName, columnParticipantEmail;
  @FXML
  private TableView<User> tableParticipants;
  @FXML
  public Label labelError;

  private final ObservableList<User> newParticipantsList = FXCollections.observableArrayList();
  private BudgetController budgetController;

  public void setBudgetController(BudgetController budgetController) {
    this.budgetController = budgetController;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initButtons();
    initParticipantsTable();
  }

  private void initButtons() {
    buttonAddParticipant.setOnAction(event ->
        DataUtils.addUserToParticipantsList(newParticipantsList, fieldEmail, labelError));
    buttonConfirm.setOnAction(event -> {
      addNewParticipantsToBudget();
      currentStage.close();
    });
    buttonCancel.setOnAction(event -> currentStage.close());
  }

  private void initParticipantsTable() {
    columnParticipantName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnParticipantEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    tableParticipants.setItems(newParticipantsList);
  }

  private void addNewParticipantsToBudget() {
    budgetController.addParticipants(newParticipantsList);
  }

  @Override
  protected void clearErrorHighlights() {
    labelError.setText("");
    ErrorHighlighter.unhighlightFields(fieldEmail);
  }
}
