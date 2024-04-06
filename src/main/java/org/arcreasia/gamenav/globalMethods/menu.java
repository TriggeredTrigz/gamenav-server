package org.arcreasia.gamenav.globalMethods;
import java.util.Scanner;

public class menu implements Runnable {

    private static Scanner sc = new Scanner(System.in);

    public static void Menu(){
        logger.logApp.info("CLI Menu initiated.");
        boolean logView=false,uptimeView=false;
        while(true){
            clearScreen.cls();
            System.out.println("||\t\t GameNav Server \t\t||");
            System.out.println("||\t\t\t Options \t\t||");
            if( !logView ) System.out.println("1. View game caching");
            else System.out.println("1. Stop viewing game caching");
            if( !uptimeView ) System.out.println("2. View uptime");
            else System.out.println("2. Stop viewing uptime");
            System.out.println("3. placeholder 2");
            System.out.println("4. exit");
            if( uptimeView ) {
                long Uptime = uptime.stopTimer();
                System.out.println("Uptime: " + (Uptime/1000) + "s " + (Uptime%1000) + "ms." );
            }
            String opt = sc.nextLine();
            switch (opt) {
                case "1" -> {
                    logView = !logView;
                    logger.loggerViewStatus(logger.logGameCache, logView);
                    if ( !logView ) logger.logApp.info("Disabled Game Cache vieweing.");
                    else logger.logApp.info("Enabled Game Cache viewing.");
                }
                case "2" -> {
                    uptimeView = !uptimeView;
                    
                    break;
                }
                case "3" -> {}
                case "4" -> { 
                    long Uptime = uptime.stopTimer();
                    System.out.println("Uptime: " + (Uptime/1000) + "s " + (Uptime%1000) + "ms." ); 
                    logger.logApp.info("Application exited.");
                    logger.logApp.info("Application runtime: " + (Uptime/1000) + "s " + (Uptime%1000) + "ms.");
                    System.exit(0); 
                }
            }
        }
    }

    @Override
    public void run() {
        Menu();
    }
    
}
