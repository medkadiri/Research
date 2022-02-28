package io.microhooks.eda.providers.scs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;


import io.microhooks.eda.Event;

public class EventProducer<U> {


    @Autowired
    private StreamBridge streamBridge;

    // public EventProducer(StreamBridge st){
    //     this.streamBridge = st;
    // }

    public void publish(Event<U> event, String stream) {
        
        streamBridge.send(stream, event);
        //send(stream, event, )

    }




    
}
