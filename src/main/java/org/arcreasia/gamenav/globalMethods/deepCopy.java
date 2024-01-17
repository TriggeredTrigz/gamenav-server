package org.arcreasia.gamenav.globalMethods;

import java.util.HashMap;

public class deepCopy {

    public static <K,V> HashMap<String,String> deepCopyHashMap(HashMap<String,String> original) {
        HashMap<String,String> copy = new HashMap<>();
        for ( HashMap.Entry<String,String> entry : original.entrySet() ) {
            copy.put( entry.getKey(), entry.getValue() );
        }
        return copy;
    }
    
}
