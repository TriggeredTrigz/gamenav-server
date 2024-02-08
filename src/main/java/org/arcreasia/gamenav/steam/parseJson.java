package org.arcreasia.gamenav.steam;

import org.arcreasia.gamenav.mysql.initSQL;
import org.arcreasia.gamenav.globalMethods.plsWait;

// for more direct approach to parsing from json file
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.File;
import java.sql.ResultSet;

import java.util.Scanner;

public class parseJson implements Runnable {

    final static Scanner sc = new Scanner(System.in);

    public static void parseSteamAppList ( File f_json ) throws Exception {
        int i=0;
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("/src/main/java/org/arcreasia/gamenav/steam/steamAppList.json");
        JsonParser parser = objectMapper.getFactory().createParser( f_json );
        while ( parser.nextToken() != JsonToken.START_ARRAY ) {}
        while ( parser.nextToken() == JsonToken.START_OBJECT ) {

            ObjectNode node = objectMapper.readTree(parser);

            System.out.println(node.get("appid"));
            int appid = node.get("appid").asInt();
            System.out.println(node.get("name"));
            String name = node.get("name").asText();
            
            try {
                ResultSet rs = initSQL.stmt.executeQuery("select * from steamapplist where appid = " + appid );
                if ( !rs.isBeforeFirst() ) {
                    System.out.println("insert ignore into steamAppList(appID,name) values (" + appid + ",\"" + name + "\")");
                    initSQL.stmt.executeUpdate("insert ignore into steamAppList(appID,name) values (" + appid + ",\"" + name + "\")");
                    System.out.println("Game cached.");
                } else {
                    System.out.println("Game already cached.");
                }
            } catch (Exception e) { e.printStackTrace(); }

            i++;

            if ( i == 10 ) { plsWait.plsWaitBro(2500); i=0; }

        }
        if ( parser.nextToken() != JsonToken.START_ARRAY ) {
            throw new IllegalStateException("Expected an Array");
        }
    }

    @Override
    public void run() {
        File f = new File(initSQL.env.get("steamJsonPath"));
        try { parseSteamAppList( f ); } catch (Exception e) { e.printStackTrace(); }
    }
    
}

class steamAppListLayout {
    int appID;
    String name;
}
