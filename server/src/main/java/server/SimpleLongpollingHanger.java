package server;

import common.LongpollingHanger;

/**
 * Created by glapul on 07.06.15.
 */
public class SimpleLongpollingHanger implements LongpollingHanger{
   private  Object hanger = new Object();

   public void hang() {
      System.out.println("Something hangs");
      synchronized (hanger) {
         while(true) {
            try{hanger.wait();}
            catch (InterruptedException ie) {System.out.println("Something unhangs");break;}
         }
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
