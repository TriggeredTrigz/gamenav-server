/*
 *
 * Purpose of file:
 * 
 *      parse json files as necessary for database
 * 
 */

package org.arcreasia.gamenav.steam;

import org.arcreasia.gamenav.mysql.initSQL;
import org.arcreasia.gamenav.globalMethods.callAPI;
import org.arcreasia.gamenav.globalMethods.jsonFuncs;
import org.arcreasia.gamenav.globalMethods.logger;
import org.arcreasia.gamenav.globalMethods.plsWait;

// for more direct approach to parsing from json
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.sql.ResultSet;

import java.util.Scanner;
// import javax.json.Json;

public class dataCaching implements Runnable {

    final static Scanner sc = new Scanner(System.in);

    private jsonFuncs jsonFuncs = new jsonFuncs();

    static StringBuffer url;
    static StringBuffer gameDetails = new StringBuffer("");

    static ObjectMapper objectMapper = new ObjectMapper();

    public static void parseSteamListWCheck ( String json ) throws Exception {

        JsonParser listParser = objectMapper.getFactory().createParser( json );
        while( listParser.nextToken() != JsonToken.START_ARRAY ) {}
        while( listParser.nextToken() == JsonToken.START_OBJECT ) {
            ObjectNode listNode = objectMapper.readTree(listParser);
            int appid = listNode.get("appid").asInt();
            
            try {
                // pre check for already added records
                initSQL.stmt.executeUpdate("insert into dbnav.steamlist(appid,type) values(" + appid + ",\"ph\");");

                gameDetails.setLength(0);
                try {
                    gameDetails.append( callAPI.apiGetResponse( "https://store.steampowered.com/api/appdetails?appids=" + appid ) );
                    if( gameDetails.substring(0, 3).equals("Err") ) {
                        logger.logGameCache.info("Connection failed. HTTP Response:" + gameDetails.substring(6, gameDetails.length()));
                    }
                } catch (Exception e) { 
                    // e.printStackTrace(); 
                    logger.logGameCache.info(e.toString());
                }
                    
                JsonParser gameParser = objectMapper.getFactory().createParser( gameDetails.toString() );
                ObjectNode gameNode = null,dataNode = null;
                if( gameParser.nextToken() == JsonToken.START_OBJECT ) {
                    gameNode = objectMapper.readTree(gameParser);
                }
                gameParser = objectMapper.getFactory().createParser( gameNode.get(String.valueOf(appid)).toString() );
                gameNode = objectMapper.readTree(gameParser);
                if( gameNode.get("success").asText().equals("false") ) { 
                    // if( 0 != initSQL.stmt.executeUpdate("insert into dbnav.steamlist(appid,type) values(" + appid + ",\"invalid\");") )
                    if( 0!= initSQL.stmt.executeUpdate("update dbnav.steamlist set type=\"invalid\" where appid="+ appid) )
                    logger.logGameCache.info("Appid:" + appid + "\tname:" + listNode.get("name").asText() + "\t\t\tnot valid.");
                    plsWait.plsWaitBro(dataCaching.class,5000);
                    continue;
                }
                else { 
                    JsonParser dataParser = objectMapper.getFactory().createParser( gameNode.get("data").toString() );
                    dataNode = objectMapper.readTree(dataParser);
                }

                String name = listNode.get("name").asText();
                StringBuffer name_buffer = new StringBuffer("");
                for ( int j = 0; j < name.length(); j++){
                    char c = name.charAt(j);
                    if( c == '"' ) name_buffer.append("\"");
                    name_buffer.append( String.valueOf( c ) );
                }
                name = name_buffer.toString();

                String type;
                switch ( dataNode.get("type").asText() ) {
                    case "game" -> { type = "game"; }
                    case "demo" -> { type = "demo"; }
                    case "dlc" -> { type = "dlc"; }
                    default -> { type = "app"; }
                }
                // if( 0 != initSQL.stmt.executeUpdate("insert into dbnav.steamlist(appid,name,type) values(" + appid + ",\"" + name + "\",\"" + type + "\");") )
                if( 0!= initSQL.stmt.executeUpdate("update dbnav.steamlist set name=\"" + name + "\",type=\"" + type + "\" where appid="+ appid +";") )
                logger.logGameCache.info("Appid:" + appid + "\t name:" + name + "\t\t\tcached " + type);
                plsWait.plsWaitBro(dataCaching.class,5000);
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                logger.logGameCache.info("Appid:" + appid + "\t name:" + listNode.get("name").asText() + "\t\t\talready cached.");
                plsWait.plsWaitBro(dataCaching.class,64);
            } catch (Exception e) { 
                logger.logGameCache.info(e.toString());
            } 
        }
    }

    public void parseSteamList (String json) throws Exception {
        
        JsonParser listParser = objectMapper.getFactory().createParser(json);
        while( listParser.nextToken() != JsonToken.START_ARRAY ) {}
        while( listParser.nextToken() == JsonToken.START_OBJECT ) {
            ObjectNode listNode = objectMapper.readTree(listParser);
            int appid = listNode.get("appid").asInt();

            try{
                // pre check for already added records
                initSQL.stmt.executeUpdate("insert into dbnav.steamlist(appid,type) values(" + appid + ",\"ph\");");
                gameDetails.setLength(0);
                try{
                    gameDetails.append( callAPI.apiGetResponse( "https://store.steampowered.com/api/appdetails?appids=" + appid ) );
                    if( gameDetails.substring(0, 3).equals("Err") ) {
                        logger.logGameCache.info("Connection failed. HTTP Response:" + gameDetails.substring(6, gameDetails.length()) );
                    }
                } catch (Exception e) {
                    logger.logGameCache.info(e.toString());
                }

                if( jsonFuncs.nodeData(gameDetails.toString(), "success").equals("false") ) {
                    if( 0!= initSQL.stmt.executeUpdate("update dbnav.steamlist set type=\"invalid\" where appid="+ appid) )
                    logger.logGameCache.info("Appid:" + appid + "\tname:" + listNode.get("name").asText() + "\t\t\tnot valid.");
                    plsWait.plsWaitBro(dataCaching.class,5000);
                    continue;
                } 
                else {
                    String name = jsonFuncs.nodeData(gameDetails.toString(), "name");
                    StringBuffer name_buffer = new StringBuffer("");
                    for ( int j = 0; j < name.length(); j++){
                        char c = name.charAt(j);
                        if( c == '"' ) name_buffer.append("\"");
                        name_buffer.append( String.valueOf( c ) );
                    }
                    name = name_buffer.toString();

                    String type = jsonFuncs.nodeData(gameDetails.toString(), "type");
                    
                }

            } catch (java.sql.SQLIntegrityConstraintViolationException SQLError) {
                logger.logGameCache.info("Appid:" + appid + "\t name" + listNode.get("name").asText() + "\t\t\t already cached.");
                plsWait.plsWaitBro(dataCaching.class, 64);
            } catch (Exception e) {
                logger.logGameCache.info(e.toString());
            }
        }
    }

    public static void parseSteamAppList ( String json ) throws Exception {
        int i=0;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser parser = objectMapper.getFactory().createParser( json );
        while( parser.nextToken() != JsonToken.START_ARRAY ) {}
        while( parser.nextToken() == JsonToken.START_OBJECT ) {

            ObjectNode node = objectMapper.readTree(parser);

            int appid = node.get("appid").asInt();
            String name = node.get("name").asText();
            StringBuffer name_buffer = new StringBuffer("");
            for ( int j = 0; j < name.length(); j++){
                char c = name.charAt(j);
                if( c == '"' ) name_buffer.append("\"");
                name_buffer.append( String.valueOf( c ) );
            }
            name = name_buffer.toString();
            String banner = "https://steamcdn-a.akamaihd.net/steam/apps/" + appid + "/header.jpg";
            
            try {
                ResultSet rs = initSQL.stmt.executeQuery("select * from steamapplist where appid = " + appid );
                if( !rs.isBeforeFirst() ) {
                    initSQL.stmt.executeUpdate("insert into steamAppList(appID,name,banner) values (" + appid + ",\"" + name + "\",\"" + banner + "\") on duplicate key update banner = \"" + banner + "\"");
                    // System.out.println("Game cached.");
                } else {
                    // System.out.println("Game already cached.");
                }
            } catch (Exception e) { e.printStackTrace(); }

            i++;

            if( i == 30 ) { plsWait.plsWaitBro(dataCaching.class,2500); i=0; }

        }
        if( parser.nextToken() != JsonToken.START_ARRAY ) {
            throw new IllegalStateException("Expected an Array");
        }
    }

    public StringBuffer parseGameDetails ( int appid ) {

        url.append("https://store.steampowered.com/api/appdetails?appids=" + appid);

        try {
            gameDetails.append( callAPI.apiGetResponse( url.toString() ) );
        } catch (Exception e) { e.printStackTrace(); }

        url.setLength(0);
        return gameDetails;
    }

    @Override
    public void run() {
        try {
            logger.logApp.info("Initialiizing steam app list caching method.");
            logger.logApp.info("Getting AppList json from Steam API: https://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json");
            String json = new String(callAPI.apiGetResponse("https://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json"));
            if( json.substring(0, 3).equals("Err") ) {
                logger.logGameCache.info("Connection to https://api.steampowered.com/ISteamApps/GetAppList/v0002/?format=json failed. HTTP Response:" + json.substring(6, json.length()));
                Thread.interrupted();
            }
            else {
                logger.logApp.info("Json data received.");
                logger.logApp.info("Initialiizing steam app list caching.");
                parseSteamListWCheck( json ); 
                // parseSteamList(json);
            }
        } catch (Exception e) { 
            logger.logApp.info("Stopping steam game cache thread.");
            logger.logApp.info(e.toString()); 
            Thread.interrupted();
        }
    }
    
}

