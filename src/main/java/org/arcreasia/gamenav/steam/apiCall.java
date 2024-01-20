package org.arcreasia.gamenav.steam;

// import java.lang.reflect.Field;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class apiCall {

    // link for full applist: http://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json

    static final String steampowered_baseURL = "http://api.steampowered.com/";
    static StringBuffer passURL=new StringBuffer(steampowered_baseURL);

    static String responseJson;

    parseJson parseJson = new parseJson();

    public void apiCallReq (int n) {
        // if 0, get app list and compare and/or save to applist
        switch(n){
            case 0 -> {
                passURL.append("ISteamApps/GetAppList/v0002/?format=json");
                getResponse();
            }
        }
    }

    void getResponse() {
        try {
            responseJson = apiGetResponse();
        } catch (Exception e) { e.printStackTrace(); }

        passURL.replace(0, passURL.length(), steampowered_baseURL);
    }

    String apiGetResponse() throws Exception {

        String passURL_nobuffer = new String(passURL); // String with no buffer, since URL/I method doesn't support StringBuffer
    
        // url link for get request
        URL get_url=URI.create(passURL_nobuffer).toURL(); 
    
        // initiating http connection
        HttpURLConnection con = (HttpURLConnection) get_url.openConnection();
        con.setDefaultUseCaches(false);
        con.setRequestMethod("GET");
        
        //checks if connection is established
        int responseCode=con.getResponseCode(); 
        System.out.println(responseCode);
        if (responseCode==HttpsURLConnection.HTTP_OK) {
            
            // some stuff to get the json data response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()),responseCode);
            StringBuffer uncleanResponse = new StringBuffer();
            String s;
            while( ( s = in.readLine() ) != null) uncleanResponse.append(s);
            // cleanResponse = "["+uncleanResponse.replaceAll("^\"|\"$","^[]$")+"]"; // replaced "" with [] in json String
            // cleanResponse = uncleanResponse.replaceAll("^\"|\"$","^[]$"); // removed heading and trailing "" from json String
            responseJson = uncleanResponse.toString();
            in.close();
            
        } else {responseJson=null;} // placeholder

        con.disconnect(); // close connection
        return responseJson;
    }

}
