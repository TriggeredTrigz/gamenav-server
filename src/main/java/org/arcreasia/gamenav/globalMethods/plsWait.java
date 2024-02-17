package org.arcreasia.gamenav.globalMethods;

import org.arcreasia.gamenav.steam.parseJSON;

public class plsWait {

    static parseJSON parseJson = new parseJSON();

    public static void plsWaitBro(int n){

        synchronized(parseJson) { 
            try { parseJson.wait(n); } catch (Exception e) { e.printStackTrace(); }
        }

    }

}
