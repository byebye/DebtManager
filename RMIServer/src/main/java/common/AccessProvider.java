package common;

import server.SimpleMessageBox;

import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by glapul on 10.05.15.
 */

public interface AccessProvider extends Remote {

    public Object getAccess(String login, String passwordHash)
            throws RemoteException, AuthenticationException;
}