package org.arcreasia.gamenav.globalMethods;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class logger {

    public static Logger logApp = Logger.getLogger("Application Log");
    public static Logger logGameCache = Logger.getLogger("Game Cache Log");
    public static Logger logHTTPreq = Logger.getLogger("HTTP Log");
    private static FileHandler fh_app, fh_gc, fh_http;

    public static void loggerViewStatus(Logger logger, boolean view) {
        logger.setUseParentHandlers(view);
    }

    public static void initLogger() {
        try {

            // app logs
            logApp.setUseParentHandlers(false); 
            fh_app = new FileHandler("resources/cache/application.log", true);
            logApp.addHandler(fh_app); 
            
            // game caching logs
            logGameCache.setUseParentHandlers(false);
            fh_gc = new FileHandler("resources/cache/gameCache.log", true);
            logGameCache.addHandler(fh_gc);
            
            // http requests logs
            logHTTPreq.setUseParentHandlers(false);
            fh_http = new FileHandler("resources/cache/httpRequests.log");
            logHTTPreq.addHandler(fh_http);
            
            // log formatting
            SimpleFormatter formatter = new SimpleFormatter();
                fh_app.setFormatter(formatter); 
                fh_gc.setFormatter(formatter);
                fh_http.setFormatter(formatter);

        } catch (Exception e) { 
            logApp.info("Errored. Check logs."); 
            logApp.info( e.toString() );
            // e.printStackTrace(); 
        }
    }
}
