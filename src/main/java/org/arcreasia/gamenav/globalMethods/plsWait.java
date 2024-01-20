package org.arcreasia.gamenav.globalMethods;

import org.arcreasia.gamenav.steam.parseJson;

public class plsWait {

    static parseJson parseJson = new parseJson();

    public static void plsWaitBro(int n){

        synchronized(parseJson) { 
            try { parseJson.wait(n); } catch (Exception e) { e.printStackTrace(); }
        }

    }

}
