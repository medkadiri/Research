package io.microhooks.eda.providers.scs;


import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import io.microhooks.eda.Event;

@Component
public class EventProducer<U> {


    private StreamBridge streamBridge;

    public EventProducer(StreamBridge st){
        this.streamBridge = st;
    }

    public void publish(Event<U> event, String stream) {
        
        streamBridge.send(stream, event);
        //send(stream, event, )

    }




    
}
