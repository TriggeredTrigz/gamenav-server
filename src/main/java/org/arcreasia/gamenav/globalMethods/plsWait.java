package org.arcreasia.gamenav.globalMethods;

import org.arcreasia.gamenav.steam.parseJSON;

public class plsWait {

    static parseJSON parseJson = new parseJSON();

    public static void plsWaitBro(int Millis){

        synchronized(parseJson) { 
            try { parseJson.wait(Millis); } catch (Exception e) { e.printStackTrace(); }
        }

    }

}
