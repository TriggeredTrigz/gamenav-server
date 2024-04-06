package org.arcreasia.gamenav.globalMethods;

public class uptime {

    static long start,stop;
    
    public static void initTimer() {
        start = System.currentTimeMillis();
    }

    public static long stopTimer() {
        stop = System.currentTimeMillis();
        return (stop-start);
    }
}
