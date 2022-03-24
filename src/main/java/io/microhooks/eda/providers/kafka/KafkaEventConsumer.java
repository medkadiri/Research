package io.microhooks.eda.providers.kafka;

import lombok.Data;

@Data
public class KafkaEventConsumer<T, U> /*extends EventConsumer<T, U> */{

    public void itWorks(){
        System.out.println("\n\n-----Kafka Consumer is listening successfully !!! -----\n\n");
    }
    

}
