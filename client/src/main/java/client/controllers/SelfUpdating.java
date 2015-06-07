package client.controllers;

import client.UpdateLongpollingCallbackRegistrar;
import common.RemoteCallback;
import javafx.application.Platform;

/**
 * Created by glapul on 07.06.15.
 */
public interface SelfUpdating {
   void update();


   default void subscribeForUpdates() {
      RemoteCallback rc = new RemoteCallback() {
         @Override
         public void call() {
            Platform.runLater(() -> {
               SelfUpdating.this.update();
            });
         }
      };
      UpdateLongpollingCallbackRegistrar.registerCallbackOnServer(rc);
   }

   default void unsubscribeFromUpdates() {
      UpdateLongpollingCallbackRegistrar.deregisterMostRecentCallback();
   }
}
