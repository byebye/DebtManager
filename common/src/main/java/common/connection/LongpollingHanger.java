package common.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LongpollingHanger extends Remote {

  String NAME = "LongpollingHanger";

  void hang() throws RemoteException;
}
