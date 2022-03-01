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
    @KafkaListener(topics = "io.microhooks.TestEntity", groupId = "groupId")
    public void processEvent(/*long key, Event<String> event*/ String message) {
        // System.out.println("Received Event Key: " + key);
        // System.out.println("Received Event Timestamp: " + event.getTimestamp());
        // System.out.println("Received Event Username: " + event.getUsername());
        // System.out.println("Received Event Payload: " + event.getPayload());
        System.out.println("Received Message: " + message);
    }
    
}
