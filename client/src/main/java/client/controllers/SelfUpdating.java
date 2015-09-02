package client.controllers;

import client.UpdateLongpollingCallbackRegistrar;
import common.connection.RemoteCallback;
import javafx.application.Platform;
import javafx.stage.Stage;

public interface SelfUpdating {

  void update();

  Stage getCurrentStage();

  default void subscribeForUpdates() {
    RemoteCallback rc = () -> Platform.runLater(SelfUpdating.this::update);
    UpdateLongpollingCallbackRegistrar.registerCallbackOnServer(rc);
    getCurrentStage().setOnCloseRequest(event ->
        UpdateLongpollingCallbackRegistrar.deregisterMostRecentCallback());
  }
}
