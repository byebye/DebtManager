package client.controllers;

import common.connection.RemoteCallback;
import client.UpdateLongpollingCallbackRegistrar;

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
