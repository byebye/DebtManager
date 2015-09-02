
package client.controllers;

import client.utils.DataUtils;
import client.utils.DataFormatListeners;
import client.view.Alerts;
import client.view.ErrorHighlighter;
import client.utils.ImageUtils;
import common.data.Budget;
import common.data.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetCreatorController extends BasicController implements Initializable {

  @FXML
  private TextField fieldBudgetName, fieldBudgetDescription, fieldEmail;
  @FXML
  private Button buttonCreateBudget, buttonAddParticipant, buttonCancel;
  @FXML
  private TableView<User> tableParticipants;
  @FXML
  private TableColumn<User, String> columnParticipantName, columnParticipantEmail;
  @FXML
  private TableColumn<User, Boolean> columnAction;
  @FXML
  private Label labelError;

  private static final int MAX_BUDGET_NAME_LENGTH = 16;
  private static final int MAX_BUDGET_DESCRIPTION_LENGTH = 32;

  private final ObservableList<User> participantsList = FXCollections.observableArrayList();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initFields();
    initButtons();
    initParticipantsList();
  }

  private void initFields() {
    fieldBudgetName.textProperty()
        .addListener(DataFormatListeners.restrictTextLength(fieldBudgetName::setText, MAX_BUDGET_NAME_LENGTH));
    fieldBudgetDescription.textProperty()
        .addListener(
            DataFormatListeners.restrictTextLength(fieldBudgetDescription::setText, MAX_BUDGET_DESCRIPTION_LENGTH));
  }

  private void initButtons() {
    buttonAddParticipant.setOnAction(event ->
        DataUtils.addUserToParticipantsList(participantsList, fieldEmail, labelError));
    buttonCreateBudget.setOnAction(event -> createBudget());
    buttonCancel.setOnAction(event -> currentStage.close());
  }

  private void createBudget() {
    clearErrorHighlights();
    try {
      final List<User> participantsListSerializable = new ArrayList<>(participantsList);
      final Budget budget = new Budget(currentUser, fieldBudgetName.getText(), fieldBudgetDescription.getText(),
          participantsListSerializable);
      if (dbHandler.createBudget(budget))
        currentStage.close();
      else
        // TODO more specific message
        labelError.setText("Budget couldn't be created! Try again.");
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }

  private void initParticipantsList() {
    // TODO make table focus traversable
    columnParticipantName.setCellValueFactory(new PropertyValueFactory<>("name"));
    columnParticipantEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    columnAction.setCellFactory(param -> new RemoveParticipantCell());

    tableParticipants.setFocusTraversable(true);
    tableParticipants.setItems(participantsList);
    participantsList.add(currentUser);
  }

  protected void clearErrorHighlights() {
    labelError.setText("");
    ErrorHighlighter.unhighlightFields(fieldBudgetName, fieldBudgetDescription, fieldEmail);
  }

  private class RemoveParticipantCell extends TableCell<User, Boolean> {

    final Button btnRemove = ImageUtils.loadImageButton("RemoveButton.png");

    public RemoveParticipantCell() {
      setPadding(new Insets(0, 0, 0, 0));
      btnRemove.setOnAction(event -> removeParticipant());
    }

    private void removeParticipant() {
      User participant = (User) getCurrentRow().getItem();
      participantsList.remove(participant);
    }

    private TableRow getCurrentRow() {
      return RemoveParticipantCell.this.getTableRow();
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
      super.updateItem(item, empty);
      if (!empty) {
        setGraphic(btnRemove);
        if (currentUserRow())
          btnRemove.setDisable(true);
      }
      else {
        setGraphic(null);
      }
    }

    private boolean currentUserRow() {
      return getCurrentRow().getIndex() == 0;
    }
  }
}

