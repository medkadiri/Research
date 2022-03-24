package io.microhooks.eda;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public abstract class EventConsumer<T, U> {

    @Value("${appName}")
    private String appName;

    
}
