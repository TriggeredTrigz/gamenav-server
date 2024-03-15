package org.arcreasia.gamenav;

import org.arcreasia.gamenav.mysql.initSQL;
import org.arcreasia.gamenav.steam.parseJSON;
import org.arcreasia.gamenav.globalMethods.menu;
import org.arcreasia.gamenav.globalMethods.clearScreen;
import org.arcreasia.gamenav.globalMethods.logger;

import org.arcreasia.gamenav.steam.callAPI;

public class Main {

    /* initialises MySQL connection class objects */
    static initSQL initSQL = new initSQL();
    
    /* initialises JSON parsing class for Steam games */
    static parseJSON parseJson = new parseJSON();

    /* initialises menu to view various functions */
    static menu menu = new menu();

    /* initialises API requesting class */
    static callAPI apiCall = new callAPI();

    // static logger logger = new logger();
    
    public static void main(String[] args) {

        logger.initLogger();

        /* Hi server! */
        logger.logApp.info("Hello world!");

        /* clears CMD */
        // clearScreen.cls();

        /* initialises MySQL connection and database */
        org.arcreasia.gamenav.mysql.initSQL.connectSQL();
        initSQL.createDB();
        
        /* start threads */
        Thread thread_gameCaching = new Thread(parseJson);
        thread_gameCaching.start();

        /* start menu */
        Thread thread_menu = new Thread(menu);
        thread_menu.start();

        // apiCall.getResponse();

    }
}

