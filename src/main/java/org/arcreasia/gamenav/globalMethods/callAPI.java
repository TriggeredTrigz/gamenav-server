/*
 *
 * Purpose of file:
 * 
 *      Call apis and return appropriate values
 * 
 */

package org.arcreasia.gamenav.globalMethods;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import org.arcreasia.gamenav.steam.dataCaching;

public class callAPI {

    /* 
     * sample API requests:
     * 
     * link for full applist: https://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json
     * link for app details: https://store.steampowered.com/api/appdetails?appids=730
     * links for banner: https://cdn.akamai.steamstatic.com/steam/apps/730/header.jpg
     *                   https://steamcdn-a.akamaihd.net/steam/apps/730/header.jpg
     * 
     */

    dataCaching parseJson = new dataCaching();

    public static String apiGetResponse( String requestedURL ) throws Exception {
    
        // url link for get request
        URL get_url = new URI(requestedURL).toURL();
        logger.logHTTPreq.info("Requesting from:" + get_url);
    
        // initiating http connection
        HttpURLConnection con = (HttpURLConnection) get_url.openConnection();
        con.setDefaultUseCaches(false);
        con.setRequestMethod("GET");
        
        //checks if connection is established
        int responseCode=con.getResponseCode(); 
        logger.logHTTPreq.info("HTTP Response Code:" + responseCode);
        
        if (responseCode==HttpsURLConnection.HTTP_OK) {
            // some stuff to get the json data response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()),responseCode);
            StringBuffer jsonResponse = new StringBuffer();
            // while( in.readLine() != null ) jsonResponse.append(in.readLine());
            String s;
            while( ( s = in.readLine() ) != null) jsonResponse.append(s);
            con.disconnect(); // close connection
            in.close();
            return jsonResponse.toString();
        } else {
            // http request failed
            con.disconnect();
            return "Error:"+responseCode;
        } 
    }
}
