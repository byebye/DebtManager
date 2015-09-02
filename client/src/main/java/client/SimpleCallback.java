package client;

import common.connection.RemoteCallback;

/**
 * Created by glapul on 07.06.15.
 */
public class SimpleCallback  implements RemoteCallback {
   @Override
   public void call()  {
      System.out.println("Callback called");
   }
}
