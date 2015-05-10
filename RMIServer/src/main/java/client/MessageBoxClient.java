package client;

import common.AccessProvider;
import common.MessageBox;
import common.SHA1Hasher;

import javax.naming.AuthenticationException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * Created by glapul on 10.05.15.
 */
public class MessageBoxClient {

    public static void main(String [] args) {

        if(System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        Scanner sc = new Scanner(System.in);

        String name = "AccessProvider";

        try {
            AccessProvider ac = (AccessProvider) LocateRegistry.getRegistry(args[0]).lookup(name);
            MessageBox mb;

            while(true) {
                System.out.print("Login: ");
                String login = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();
                String passwordHash = SHA1Hasher.hash(password);
                try {
                    mb = (MessageBox) ac.getAccess(login, passwordHash);
                    break;
                }
                catch(AuthenticationException e) {
                    System.out.println("Wrong login/password combination");
                }
            }

            System.out.println("Access granted. Now you can put messages in the box with typing 'PUT [MESSAGE]' or retrieve them with 'GET'");

            while(true) {
                String opString = sc.next();

                if(opString.equals("PUT")) {
                    String msg = sc.next();
                    mb.updateMessage(msg);
                }

                if(opString.equals("GET")) {
                    System.out.println(mb.getMessage());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
