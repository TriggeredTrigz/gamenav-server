package org.arcreasia.gamenav;

import org.arcreasia.gamenav.mysql.initSQL;
import org.arcreasia.gamenav.steam.parseJSON;
import org.arcreasia.gamenav.globalMethods.menu;
import org.arcreasia.gamenav.globalMethods.clearScreen;

import org.arcreasia.gamenav.steam.callAPI;

public class Main {

    static initSQL initSQL = new initSQL();
    
    static parseJSON parseJson = new parseJSON();
    static menu menu = new menu();

    static callAPI apiCall = new callAPI();
    
    public static void main(String[] args) {

        clearScreen.cls();

        org.arcreasia.gamenav.mysql.initSQL.connectSQL();
        initSQL.createDB();
        
        System.out.println("Hello world!");
        
        Thread thread_gameCaching = new Thread(parseJson);
        thread_gameCaching.start();

        // Thread thread_menu = new Thread(menu);
        // thread_menu.start();

        // apiCall.getResponse();

    }
}

