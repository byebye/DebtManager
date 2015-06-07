package client;

import common.CallbackManager;
import common.LongpollingHanger;
import common.RemoteCallback;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
   }

   private static class Hanger implements Runnable {

      @Override
      public void run() {
         LongpollingHanger cmg;
         try {
           cmg = ((LongpollingHanger) LocateRegistry.getRegistry(host).lookup(LongpollingHanger.NAME));
         } catch (RemoteException | NotBoundException re) {
            re.printStackTrace();
            System.out.println("Couldn't get the CallbackManager");
            return;
         }
         System.out.println("Hanger thread running");
         while(true) {
            try {
               System.out.println("hang");
               cmg.hang();
               System.out.println("unhang");
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
         System.out.println("Maybe will call something");
         if(!callbackStack.empty()) {
            System.out.println("Yes it called something");
            callbackStack.peek().call();
         }
      }
      catch (RemoteException re) {} //it will never occur
   }
}
