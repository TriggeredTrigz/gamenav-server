package org.arcreasia.gamenav.globalMethods;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class logger {

    public static Logger logApp = Logger.getLogger("Application Log");
    public static Logger logGameCache = Logger.getLogger("Game Cache Log");
    private static FileHandler fh, fh_gc;

    public static void enableView_GameCaching(boolean view){
        logGameCache.setUseParentHandlers(view);
    }

    public static void initLogger() {
        try {
            logApp.setUseParentHandlers(false); logGameCache.setUseParentHandlers(false);
            logApp.info("Attempting log files creation...");
            fh = new FileHandler("resources/cache/application.log", true);
            fh_gc = new FileHandler("resources/cache/gameCache.log", true);
            logApp.addHandler(fh); logGameCache.addHandler(fh_gc);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter); fh_gc.setFormatter(formatter);
        } catch (Exception e) { 
            logApp.info("Errored. Check logs."); 
            logApp.info( e.toString() );
            // e.printStackTrace(); 
        }
    }
}
