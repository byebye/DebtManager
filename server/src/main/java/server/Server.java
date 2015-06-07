package server;

import common.AccessProvider;
import common.CallbackManager;
import common.RemoteCallback;
import org.jooq.util.derby.sys.Sys;

import javax.xml.crypto.Data;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

/**
 * Created by glapul on 16.05.15.
 */
public class Server {
    static DatabaseController dbController;
    private static String dbUser, dbPassword, url;

    public static void main (String [] args) {
        try {
            parseArguments(args);
            System.out.println("Connecting to " + url + " as " + dbUser);
            DatabaseController.createInstance(dbUser, dbPassword, url);
            DatabaseController.createExportedInstance();
            dbController = DatabaseController.getInstance();
        }
        catch (Exception e) {
            System.err.println("Could not connect to the database");
        }
        // set the security manager - needed for RMI
        if(System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            String name = "AccessProvider";
            AccessProvider ac = new BasicAccessProvider();
            AccessProvider stub = (AccessProvider) UnicastRemoteObject.exportObject(ac, 1100);

            Registry reg = LocateRegistry.getRegistry();
            reg.rebind(name, stub);

            SimpleUpdateManager sum = setupUpdateManager();
            runUpdateDaemon(sum);

            System.out.println("Server running");
        } catch (RemoteException rme) {
            rme.printStackTrace();
        }
    }

    private static SimpleUpdateManager setupUpdateManager() {
        String name = "UpdateManager";
        SimpleUpdateManager sum = new SimpleUpdateManager();

        try {
            CallbackManager exp = (CallbackManager) UnicastRemoteObject.exportObject(sum, 1100);
            LocateRegistry.getRegistry().rebind(name, exp);
        }
        catch(RemoteException re) {
            System.out.println("Setting up update manager failed");
            re.printStackTrace();
        }
        return sum;
    }

    public static void runUpdateDaemon(SimpleUpdateManager sum) {

        Thread updateDaemon = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        sum.callAll();
                        TimeUnit.SECONDS.sleep(5);
                    }
                    catch (InterruptedException ie) {
                        break;
                    }
                }
            }
        });
        //updateDaemon.setDaemon(true);
        updateDaemon.start();
    }

    private static void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            switch(args[i]) {
                case "-u": dbUser = args[i+1]; break;
                case "-p": dbPassword = args[i+1]; break;
                case "-d": url = "jdbc:postgresql://" + args[i+1]; break;
            }
        }
        if (dbUser == null || dbPassword == null || url == null) {
            System.out.println("Warning: missing or incorrect database settings - default will be used.");
            dbUser = "debtmanager"; // "z1111813";
            dbPassword = "debtmanager"; // "rU7i7xWoLVdh";
            url = "jdbc:postgresql://localhost/debtmanager"; // "jdbc:postgresql://db.tcs.uj.edu.pl/z1111813";
        }
    }
}
