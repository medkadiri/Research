package io.microhooks.ddd.internal;

import java.util.HashMap;
import java.util.Map;

public interface Trackable {
    public Map<String, Object> trackedFields = new HashMap<>();

    default Map<String, Object> getTrackedFields(){
        System.out.println("Hello");
        return trackedFields;
    }
}
