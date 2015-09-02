package client;

import common.connection.LongpollingHanger;
import common.connection.RemoteCallback;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Stack;

/**
 * Created by glapul on 07.06.15.
 */
public abstract class UpdateLongpollingCallbackRegistrar {
   private static String host;

   //list containing references to callbacks to prevent them from being garbage collected
   private static Stack<RemoteCallback> callbackStack = new Stack<>();

   public static void setHost(String host) {
      UpdateLongpollingCallbackRegistrar.host = host;
   }

   public static void start() {
      new Thread(new Hanger()).start();
   }

   public static void deregisterAll() {
      //TODO will deregister objects and free the ports;
   }

   public synchronized static void registerCallbackOnServer(RemoteCallback rc) {
      if (host == null)
         throw new IllegalArgumentException("no host specified");

      callbackStack.push(rc);
      System.out.println("Callback registered");
   }

   public synchronized static void deregisterMostRecentCallback() {
      callbackStack.pop();
      if(!callbackStack.empty()) {
         try {
            callbackStack.peek().call();
         } catch (Exception e) {}
      }
   }

   private static class Hanger implements Runnable {

      @Override
      public void run() {
         LongpollingHanger cmg;
         try {
           cmg = ((LongpollingHanger) LocateRegistry.getRegistry(host).lookup(LongpollingHanger.NAME));
         } catch (RemoteException | NotBoundException re) {
            re.printStackTrace();
            System.out.println("Couldn't get the longpollingCallbackManager");
            return;
         }
         System.out.println("Hanger thread running");
         while(true) {
            try {
               cmg.hang();
               UpdateLongpollingCallbackRegistrar.call();
            }
            catch (Exception e) {
               e.printStackTrace();
               System.out.println("disabling auto-updates due to connection failure");
               break;
            }
         }
      }
   }

   private static synchronized void call() {

      try {
         if(!callbackStack.empty())
            callbackStack.peek().call();
      }
      catch (Exception e) {
         deregisterMostRecentCallback();
      }
   }
}
