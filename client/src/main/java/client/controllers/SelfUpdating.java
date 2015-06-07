package client.controllers;

import client.UpdateLongpollingCallbackRegistrar;
import common.RemoteCallback;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Created by glapul on 07.06.15.
 */
public interface SelfUpdating {

   void update();
   Stage getStage();

   default void subscribeForUpdates() {
      RemoteCallback rc = new RemoteCallback() {
         @Override
         public void call() {
            Platform.runLater(() -> {
               SelfUpdating.this.update();
            });
         }
      };
      //UpdateLongpollingCallbackRegistrar.registerCallbackOnServer(rc);
      //getStage().setOnCloseRequest(event -> {UpdateLongpollingCallbackRegistrar.deregisterMostRecentCallback();});
   }

}
