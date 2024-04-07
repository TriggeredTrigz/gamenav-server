package org.arcreasia.gamenav.globalMethods;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class jsonFuncs {

    ObjectMapper objectMapper = new ObjectMapper();
    String returnVal = null;

    public String nodeData (String json, String node) throws Exception {
        JsonParser parser = objectMapper.getFactory().createParser(json);
        if( parser.nextToken() != JsonToken.START_OBJECT ) return null;
        ObjectNode parserNode = objectMapper.readTree(parser);
        if( parserNode.get(node) == null ) {
            Iterator<JsonNode> childNodes = parserNode.elements();
            while ( childNodes.hasNext() && returnVal == null ) {
                returnVal = nodeData(childNodes.next().toString(), node);
            }
        }
        else {
            return parserNode.get(node).toString();
        }
        return returnVal;
    }
    
}
