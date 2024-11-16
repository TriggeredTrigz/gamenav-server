package org.arcreasia.gamenav;

import org.arcreasia.gamenav.mysql.initSQL;
import org.arcreasia.gamenav.steam.dataCaching;
import org.arcreasia.gamenav.globalMethods.menu;
import org.arcreasia.gamenav.globalMethods.uptime;
import org.arcreasia.gamenav.globalMethods.callAPI;
import org.arcreasia.gamenav.globalMethods.jsonFuncs;
import org.arcreasia.gamenav.globalMethods.logger;
import org.arcreasia.gamenav.epic.epicWebScraper;
import org.arcreasia.gamenav.frontend.*;

@SuppressWarnings("unused")
public class Main {

    /* initialises MySQL connection class objects */
    private static initSQL initSQL = new initSQL();
    
    /* initialises JSON parsing class for Steam games */
    private static dataCaching dataCaching = new dataCaching();

    /* initialises menu to view various functions */
    private static menu menu = new menu();

    /* initialises API requesting class */
    private static callAPI apiCall = new callAPI();

    // static frontend_management frontend = new frontend_management();

    public static void main(final String[] args) {
        uptime.initTimer();
        logger.initLogger();

        /* Hi server! */
        logger.logApp.info("Hello world!");

        /* initialises MySQL connection and database */
        org.arcreasia.gamenav.mysql.initSQL.connectSQL();
        // initSQL.createDB();

        // /* initialize front end for management */
        // Thread thread_frontend = new Thread(frontend);
        // thread_frontend.start();

        // epicWebScraper epicWebScraper = new epicWebScraper();
        // epicWebScraper.getEpicGames();
        
        /* start steam app list caching thread */
        Thread thread_gameCaching = new Thread(dataCaching);
        thread_gameCaching.start();

        /* start menu thread */
        Thread thread_menu = new Thread(menu);
        thread_menu.start();

    }
}

