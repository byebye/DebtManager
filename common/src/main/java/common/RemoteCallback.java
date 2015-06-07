package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

/**
 * Created by glapul on 07.06.15.
 */
public interface RemoteCallback {
   void call();
}
