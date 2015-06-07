package common;

import java.rmi.Remote;

/**
 * Created by glapul on 07.06.15.
 */
public interface CallbackManager extends Remote{
   void register(RemoteCallback uc);
   void unregister(RemoteCallback uc);
   void callAll();

}
