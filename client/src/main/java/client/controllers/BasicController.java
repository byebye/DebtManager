package client.controllers;

import common.connection.AccessProvider;
import common.connection.DbHandler;
import common.data.User;
import javafx.stage.Stage;

public abstract class BasicController {

  public static AccessProvider accessProvider;
  public static DbHandler dbHandler;
  public static User currentUser;
  protected Stage currentStage;

  public void setCurrentStage(Stage stage) {
    currentStage = stage;
  }

  public Stage getCurrentStage() { return currentStage; }

  protected abstract void clearErrorHighlights();
}
