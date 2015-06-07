package server;

import common.LongpollingHanger;

/**
 * Created by glapul on 07.06.15.
 */
public class SimpleLongpollingHanger implements LongpollingHanger{
   private  Object hanger = new Object();

   public void hang() {
      synchronized (hanger) {
         while(true) {
            try{wait();}
            catch (InterruptedException ie) {break;}
         }
      }
   }

   public void unhangAll() {
      synchronized (hanger) {
         hanger.notifyAll();
      }
   }
}
