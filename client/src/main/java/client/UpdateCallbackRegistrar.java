package client;

import common.CallbackManager;
import common.RemoteCallback;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by glapul on 07.06.15.
 */
public abstract class UpdateCallbackRegistrar {
   private static String host;

   //list containing references to callbacks to prevent them from being garbage collected
   private static ArrayList<RemoteCallback> callbackList = new ArrayList<>();

   public static void setHost(String host) {
      UpdateCallbackRegistrar.host = host;
   }

   public static void deregister() {
      //TODO will deregister objects and free the ports;
   }

   public static void registerCallbackOnServer(RemoteCallback rc) {
      if(host == null)
         throw new IllegalArgumentException("no host specified");

      callbackList.add(rc);

      try {
         try {
            UnicastRemoteObject.exportObject(rc, 0);
         }
         catch (Exception e) {
            e.printStackTrace();
         }
         ((CallbackManager) LocateRegistry.getRegistry(host).lookup("UpdateManager")).register(rc);
         System.out.println("Callback registered");
      }
      catch (RemoteException |NotBoundException re) {
         System.out.println("Cannot register callback");
         re.printStackTrace();
      }
   }
}
