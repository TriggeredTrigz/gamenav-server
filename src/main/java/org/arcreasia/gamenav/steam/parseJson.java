/*

 * Purpose of file:
 * 
 *      parse json files as necessary for database
 * 
 */

package org.arcreasia.gamenav.steam;

import org.arcreasia.gamenav.mysql.initSQL;
import org.arcreasia.gamenav.globalMethods.plsWait;
import org.arcreasia.gamenav.globalMethods.uptime;

import org.arcreasia.gamenav.steam.callAPI;

// for more direct approach to parsing from json file
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.File;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Scanner;

public class parseJSON implements Runnable {

    final static Scanner sc = new Scanner(System.in);

    static StringBuffer url;
    static StringBuffer gameDetails = new StringBuffer("");

    static ObjectMapper objectMapper = new ObjectMapper();

    public static void parseSteamListWCheck ( File f_json ) throws Exception {

        JsonParser listParser = objectMapper.getFactory().createParser( f_json );
        while ( listParser.nextToken() != JsonToken.START_ARRAY ) {}
        while ( listParser.nextToken() == JsonToken.START_OBJECT ) {
            ObjectNode listNode = objectMapper.readTree(listParser);
            int appid = listNode.get("appid").asInt();
            
            try {
                ResultSet rs = initSQL.stmt.executeQuery("select * from dbnav.steamlist where appid = " + appid );
                gameDetails.setLength(0);
                if ( !rs.isBeforeFirst() ) {
                    try {
                        gameDetails.append( callAPI.apiGetResponse( "https://store.steampowered.com/api/appdetails?appids=" + appid ) );
                    } catch (Exception e) { e.printStackTrace(); }
                    
                    JsonParser gameParser = objectMapper.getFactory().createParser( gameDetails.toString() );
                    ObjectNode gameNode = null;
                    if ( gameParser.nextToken() == JsonToken.START_OBJECT ) {
                        gameNode = objectMapper.readTree(gameParser);
                    }
                    gameParser = objectMapper.getFactory().createParser( gameNode.get(String.valueOf(appid)).toString() );
                    gameNode = objectMapper.readTree(gameParser);
                    if ( gameNode.get("success").asText().equals("false") ) { 
                        initSQL.stmt.executeUpdate("insert into dbnav.steamlist(appid,type) values(" + appid + ",\"invalid\");");
                        System.out.println("Appid:" + appid + "\tname:" + listNode.get("name").asText() + "\t\t\tnot valid.");
                        plsWait.plsWaitBro(5000);
                        continue; 
                    }
                    else { 
                        // System.out.println(gameNode.get("data"));
                        gameParser = objectMapper.getFactory().createParser( gameNode.get("data").toString() );
                        gameNode = objectMapper.readTree(gameParser);
                    }

                    String name = listNode.get("name").asText();
                    StringBuffer name_buffer = new StringBuffer("");
                    for ( int j = 0; j < name.length(); j++){
                        char c = name.charAt(j);
                        if ( c == '"' ) name_buffer.append("\"");
                        name_buffer.append( String.valueOf( c ) );
                    }
                    name = name_buffer.toString();

                    String type;
                    switch ( gameNode.get("type").asText() ) {
                        case "game" -> { type = "game"; }
                        case "demo" -> { type = "demo"; }
                        case "dlc" -> { type = "dlc"; }
                        default -> { type = "app"; }
                    }
                    initSQL.stmt.executeUpdate("insert into dbnav.steamlist(appid,name,type) values(" + appid + ",\"" + name + "\",\"" + type + "\");");
                    System.out.println("Appid:" + appid + "\t name:" + name + "\t\t\tcached " + type);
                    plsWait.plsWaitBro(5000);
                }
                else {
                    System.out.println("Appid:" + appid + "\t name:" + listNode.get("name").asText() + "\t\t\talready cached.");
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // private static ObjectNode jsonIndexer ( JsonParser parser ) throws Exception {
    //     if ( parser.nextToken() != JsonToken.START_ARRAY ) {}
    //     if ( parser.nextToken() == JsonToken.START_OBJECT ) {
    //         ObjectNode node = objectMapper.readTree(parser);

    //         return node;
    //     }
    //     return null;
    // }

    public static void parseSteamAppList ( File f_json ) throws Exception {
        int i=0;

        ObjectMapper objectMapper = new ObjectMapper();
        // System.out.println("/src/main/java/org/arcreasia/gamenav/steam/steamAppList.json");
        JsonParser parser = objectMapper.getFactory().createParser( f_json );
        while ( parser.nextToken() != JsonToken.START_ARRAY ) {}
        while ( parser.nextToken() == JsonToken.START_OBJECT ) {

            ObjectNode node = objectMapper.readTree(parser);

            // System.out.println(node.get("appid"));
            int appid = node.get("appid").asInt();
            // System.out.println(node.get("name"));
            String name = node.get("name").asText();
            StringBuffer name_buffer = new StringBuffer("");
            for ( int j = 0; j < name.length(); j++){
                char c = name.charAt(j);
                if ( c == '"' ) name_buffer.append("\"");
                name_buffer.append( String.valueOf( c ) );
            }
            name = name_buffer.toString();
            String banner = "https://steamcdn-a.akamaihd.net/steam/apps/" + appid + "/header.jpg";
            
            try {
                ResultSet rs = initSQL.stmt.executeQuery("select * from steamapplist where appid = " + appid );
                if ( !rs.isBeforeFirst() ) {
                    // System.out.println("insert into steamAppList(appID,name,banner) values (" + appid + ",\"" + name + "\",\"" + banner + "\") on duplicate key update banner = \"" + banner + "\"");
                    initSQL.stmt.executeUpdate("insert into steamAppList(appID,name,banner) values (" + appid + ",\"" + name + "\",\"" + banner + "\") on duplicate key update banner = \"" + banner + "\"");
                    // System.out.println("Game cached.");
                } else {
                    // System.out.println("Game already cached.");
                }
            } catch (Exception e) { e.printStackTrace(); }

            i++;

            if ( i == 30 ) { plsWait.plsWaitBro(2500); i=0; }

        }
        if ( parser.nextToken() != JsonToken.START_ARRAY ) {
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
        uptime.initTimer();
        File f = new File(initSQL.env.get("steamJsonPath"));
        // try { parseSteamAppList( f ); } catch (Exception e) { e.printStackTrace(); }
        try {
            parseSteamListWCheck( f ); 
        } catch (Exception e) { e.printStackTrace(); }
    }
    
}

class steamAppListLayout {
    int appID;
    String name;
}
