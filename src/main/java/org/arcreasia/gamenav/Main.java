package org.arcreasia.gamenav;

import org.arcreasia.gamenav.mysql.initSQL;
import org.arcreasia.gamenav.steam.parseJson;
import org.arcreasia.gamenav.globalMethods.clearScreen;

public class Main {

    static initSQL initSQL = new initSQL();
    
    static parseJson parseJson = new parseJson();
    
    public static void main(String[] args) {

        clearScreen.cls();

        org.arcreasia.gamenav.mysql.initSQL.connectSQL();
        initSQL.createDB();
        
        System.out.println("Hello world!");
        
        Thread thread = new Thread(parseJson);
        thread.start();

    }
}

