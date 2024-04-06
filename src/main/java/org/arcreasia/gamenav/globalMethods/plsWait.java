package org.arcreasia.gamenav.globalMethods;

public class plsWait {

    public static void plsWaitBro(Object obj, int Millis){

        synchronized(obj) {
            try { obj.wait(Millis); } catch (Exception e) { logger.logApp.info(e.toString()); }
        }

    }

}
