package io.microhooks.ddd.internal;

import java.util.HashMap;
import java.util.Map;

//import javafx.util.Map;

public class ObjectsRegistry {

    private static Map<Map<Long, String>, Map<String, Object>> registry = new HashMap<>();

    public static Map<String, Object> getMap(Map<Long, String> keyMap){
        return registry.get(keyMap);
    }

    public static void putMap(Map<Long, String> keyMap, Map<String, Object> map){
        registry.put(keyMap, map);
    }
}
