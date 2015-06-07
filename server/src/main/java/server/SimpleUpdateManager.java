package server;

import common.CallbackManager;
import common.RemoteCallback;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;

/**
 * Created by glapul on 07.06.15.
 */
public class SimpleUpdateManager implements CallbackManager {

   private HashSet<RemoteCallback> callbacks;

   public SimpleUpdateManager() {
      callbacks = new HashSet<>();
   }

   @Override
   public synchronized void register(RemoteCallback uc) {
      callbacks.add(uc);
      System.out.println("Callback registered");
   }

   @Override
   public synchronized void unregister(RemoteCallback uc) {
      callbacks.remove(uc);
   }

   @Override
   public synchronized void callAll() {

      ArrayList<RemoteCallback> toDelete = new ArrayList<>();

      for(RemoteCallback rc : callbacks) {
         try {
            rc.call();
         } catch (RemoteException re) {
            toDelete.add(rc);
         }
      }

      for(RemoteCallback rc :toDelete)
        unregister(rc);

      System.out.println("Called all listeners");
   }
}
