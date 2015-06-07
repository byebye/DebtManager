package server;

import common.LongpollingHanger;

import java.util.concurrent.TimeUnit;

/**
 * Created by glapul on 07.06.15.
 */
public class SimpleLongpollingHanger implements LongpollingHanger{
   private final Object hanger = new Object();

   public void hang() {
      System.out.println("Something hangs");
      synchronized (hanger) {
         try{hanger.wait();}
         catch (InterruptedException ie) {System.out.println("Something unhangs");}
      }
   }

   public void unhangAll() {
      System.out.println("In a moment i will unhang something");
      synchronized (hanger) {
         System.out.println("I unhanged something");
         hanger.notifyAll();
      }

   }
}
