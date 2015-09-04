package server;

import common.connection.LongpollingHanger;

public class SimpleLongpollingHanger implements LongpollingHanger {

  private final Object hanger = new Object();

  public void hang() {
    synchronized (hanger) {
      try {
        hanger.wait();
      }
      catch (InterruptedException ie) {
      }
    }
  }

  public synchronized void unhangAll() {
    synchronized (hanger) {
      hanger.notifyAll();
    }
  }
}
