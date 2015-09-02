package common.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by glapul on 07.06.15.
 */
public interface LongpollingHanger extends Remote {
   String NAME = "LongpollingHanger";
   void hang() throws RemoteException;
}
