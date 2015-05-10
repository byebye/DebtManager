package server;

import javax.naming.AuthenticationException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.MessageBox;
/**
 * Created by glapul on 10.05.15.
 */
public class SimpleAccessProvider implements common.AccessProvider{

    @Override
    public MessageBox getAccess(String login,  String passwordHash) throws RemoteException, AuthenticationException {

        System.out.print("Connection attempted with login = " + login);
        // in real code it will not look like that :)
        if(login.equals("glapul") && common.SHA1Hasher.hash("zlerocta").equals(passwordHash)) {
            System.out.println(" -- GRANTED");
            return (MessageBox) UnicastRemoteObject.exportObject(new SimpleMessageBox(), 0);
        }
        else {
            System.out.println(" -- REFUSED");
            throw new AuthenticationException();
        }
    }
}
