package client;

import common.connection.RemoteCallback;

public class SimpleCallback implements RemoteCallback {

  @Override
  public void call() {
    System.out.println("Callback called");
  }
}
