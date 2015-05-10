package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by glapul on 10.05.15.
 */
public interface MessageBox extends Remote{


    public String getMessage() throws RemoteException;

    public void updateMessage(String newMessage) throws RemoteException;
}
