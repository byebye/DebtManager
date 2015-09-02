package client.utils;

import client.controllers.LoginController;
import client.view.Alerts;
import client.view.ErrorHighlighter;
import common.data.User;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.List;

public class DataUtils {

  public static void addUserToParticipantsList(List<User> participantsList, TextField fieldEmail, Label labelError) {
    ErrorHighlighter.unhighlightFields(fieldEmail);
    labelError.setText("");
    try {
      final User user = LoginController.dbHandler.getUserByEmail(fieldEmail.getText());
      if (user == null) {
        ErrorHighlighter.highlightInvalidFields(fieldEmail);
        labelError.setText("User not found!");
      }
      else if (participantsList.contains(user)) {
        ErrorHighlighter.highlightInvalidFields(fieldEmail);
        labelError.setText("User already on list");
      }
      else {
        participantsList.add(user);
        fieldEmail.clear();
      }
    }
    catch (RemoteException e) {
      e.printStackTrace();
      Alerts.serverConnectionError();
    }
  }
}
