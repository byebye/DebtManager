package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by glapul on 07.06.15.
 */
public interface CallbackManager extends Remote{
   void register(RemoteCallback uc) throws RemoteException;
   void unregister(RemoteCallback uc) throws RemoteException;
   void callAll() throws RemoteException;
}
