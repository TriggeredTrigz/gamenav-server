package org.arcreasia.gamenav.globalMethods;

public class clearScreen {

    public static final void cls() {
        try {
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
}
