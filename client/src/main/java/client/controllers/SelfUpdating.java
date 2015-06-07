package client.controllers;

import client.UpdateLongpollingCallbackRegistrar;
import common.RemoteCallback;

/**
 * Created by glapul on 07.06.15.
 */
public interface SelfUpdating {
   void update();


   default void subscribeForUpdates() {
      RemoteCallback rc = new RemoteCallback() {
         @Override
         public void call() {
            SelfUpdating.this.update();
         }
      };
      UpdateLongpollingCallbackRegistrar.registerCallbackOnServer(rc);
   }

   default void unsubscribeFromUpdates() {
      UpdateLongpollingCallbackRegistrar.deregisterMostRecentCallback();
   }
}
