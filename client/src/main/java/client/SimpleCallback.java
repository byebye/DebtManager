package client;

import common.RemoteCallback;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by glapul on 07.06.15.
 */
public class SimpleCallback extends UnicastRemoteObject implements RemoteCallback {

   protected SimpleCallback() throws RemoteException {
   }

   @Override
   public void call() throws RemoteException {
      System.out.println("Callback called");
   }
}
