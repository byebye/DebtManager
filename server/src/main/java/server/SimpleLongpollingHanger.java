package server;

import common.LongpollingHanger;

import java.util.concurrent.TimeUnit;

/**
 * Created by glapul on 07.06.15.
 */
public class SimpleLongpollingHanger implements LongpollingHanger{
   private final Object hanger = new Object();

   public void hang() {
      synchronized (hanger) {
         try{hanger.wait();}
         catch (InterruptedException ie) {}
      }
   }

   public void unhangAll() {
      synchronized (hanger) {
         hanger.notifyAll();
      }

   }
}
