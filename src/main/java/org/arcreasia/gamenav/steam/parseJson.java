package org.arcreasia.gamenav.steam;

import org.arcreasia.gamenav.globalMethods.deepCopy;

import java.util.regex.Pattern;
import java.util.HashMap;
import java.lang.reflect.Field;

import javax.json.Json;
import javax.json.stream.JsonParser;

import java.io.Reader;
import java.io.StringReader;

public class parseJson {

    public void parseGame ( Class<?> gameMetaData, String cleanResponse, int appid) throws Exception {
        
        System.out.println(cleanResponse+"\n"); 
        
        // json data object creation
        Reader json_data = new StringReader(cleanResponse); // character stream from json string
        JsonParser parser = Json.createParser(json_data); // created parser

        // Class<?> gameDetails = gameMetaData;
        Field[] declaredVars = gameMetaData.getDeclaredFields(); // gets all declared variable in gameMetaData class

        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch(event){
                case END_ARRAY, END_OBJECT, START_ARRAY, START_OBJECT, VALUE_TRUE, VALUE_FALSE, VALUE_NULL, VALUE_NUMBER, VALUE_STRING -> {}
                case KEY_NAME -> {
                    for ( Field i:declaredVars ) {
                        if ( i.getName().equals(parser.getString()) ) {
                            parser.next();
                            try {
                                i.set(gameMetaData, parser.getInt());
                            } catch (Exception e) {
                                try {
                                    i.set(gameMetaData, parser.getString());
                                } catch (Exception e1) { e.printStackTrace(); break; }
                            }
                        }
                    }
                }
            }
        }
    }

    public <K,V>HashMap<String, String> parseTop100(String cleanResponse, HashMap<String,String> hm) {

        Reader json_data = new StringReader(cleanResponse);
        JsonParser parser = Json.createParser(json_data);
        
        final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        
        boolean detailsFound;
        String gameID="",gameName="";
        int rank=0;
        breakPut:
            while ( parser.hasNext() ) {
                JsonParser.Event event = parser.next();
                detailsFound = false;
                switch (event) {
                    case END_ARRAY, END_OBJECT, START_ARRAY, START_OBJECT, VALUE_TRUE, VALUE_FALSE, VALUE_NULL, VALUE_NUMBER, VALUE_STRING -> {}
                    case KEY_NAME -> {
                        if ( pattern.matcher( parser.getString() ).matches() ) { gameID = parser.getString(); }
                        if ( parser.getString().equals("name") ) {
                            parser.next();
                            gameName = parser.getString();
                            detailsFound = true; rank++;
                        }
                        else {
                            continue breakPut;
                        }
                        if ( detailsFound ) {
                            if ( rank<10 ) hm.put("00"+rank,gameID+":"+gameName);
                            else if ( rank<100 ) hm.put("0"+rank,gameID+":"+gameName);
                            else { hm.put("100",gameID+":"+gameName); break breakPut; }
                        }
                    }
                }
            }

        return deepCopy.deepCopyHashMap(hm);
    }
    
}
