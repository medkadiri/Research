/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.microhooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.microhooks.eda.Event;
import io.microhooks.eda.EventListener;

@SpringBootApplication
@RestController
public class Test {

    @Autowired
    TestRepository repo;
    
    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @GetMapping("/hello")
    public String sayHello() {
        TestEntity entity = new TestEntity();
        System.out.println("declared fields: " + entity.getClass().getFields());
        System.out.println("class: " + entity.getClass().toString());
        //System.out.println("tracked fields: " + entity.trackedFields);
        TestEntity equality = entity;
        equality.setName("Zouine");
        System.out.println("\n\n"+ entity.getName() +"\n\n");
        if(equality == entity)
            System.out.println("\n\n-------------- Compared object succ --------------------\n\n");
        else
        System.out.println("\n\n-------------- Compared object failed --------------------\n\n");
        entity.setName("Hi!");
        entity = repo.save(entity);

        entity.setName("Hello");
        entity = repo.save(entity);

        entity.setName("Hello world!");
        entity.setName("Hello again!");
        repo.save(entity);
        
        return "Hello!";
    }

    //@EventListener(streams="Test_io.microhooks.TestEntity", label="C")
    @KafkaListener(topics = "Test_io.microhooks.TestEntity", groupId = "groupId")
    public void processEvent(/*long key, Event<String> event*/ String message) {
        // System.out.println("Received Event Key: " + key);
        // System.out.println("Received Event Timestamp: " + event.getTimestamp());
        // System.out.println("Received Event Username: " + event.getUsername());
        // System.out.println("Received Event Payload: " + event.getPayload());
        System.out.println("Received Message in group foo: " + message);
    }
    
}
