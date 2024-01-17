package org.arcreasia.gamenav.globalMethods;

public class plsWait {

    private static final Object lock = new Object();

    public static void plsWaitBro(int n){

        try{lock.wait(n);}catch(Exception e){e.printStackTrace();}

    }

}
