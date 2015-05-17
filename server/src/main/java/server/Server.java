package server;

import common.AccessProvider;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by glapul on 16.05.15.
 */
public class Server {
    private static DatabaseController dbController;
    public static void main (String [] args) {

        try {
            dbController = new DatabaseController();
        }
        catch (Exception e) {
            System.err.println("Could not connect to the database");
        }
        // set the security manager - needed for RMI
        if(System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            String name = "AccessProvider";
            AccessProvider ac = new BasicAccessProvider(dbController);
            AccessProvider stub = (AccessProvider) UnicastRemoteObject.exportObject(ac, 0);

            Registry reg = LocateRegistry.getRegistry();
            reg.rebind(name, stub);

            System.out.println("Server running");

        } catch (RemoteException rme) {
            rme.printStackTrace();
        }

    }
}
