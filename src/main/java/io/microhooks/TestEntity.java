package io.microhooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.microhooks.ddd.Source;
import io.microhooks.ddd.Track;
import io.microhooks.ddd.OnCreate;
import io.microhooks.ddd.OnUpdate;
import io.microhooks.eda.Event;
import lombok.Data;

@Entity
@Data
@Source
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Track
    private String name;

    //need to be generated when discovering that the class is annotated with source
    //private transient Map<String, Object> trackedFields = new HashMap<>();

    @OnCreate(streams = "CustomStream")
    public List<Event<TestDTO>> onCreate() {
        List<Event<TestDTO>> events = new ArrayList<>();
        events.add(new Event<>(new TestDTO(1, name), "CustomCreate"));
        return events;
    }

    @OnUpdate(streams = "CustomStream")
    public List<Event<String>> onUpdate(Map<String, Object> changedTrackedFieldsPreviousValues) {
        ArrayList<Event<String>> events = new ArrayList<>();
        String oldName = (String) changedTrackedFieldsPreviousValues.get("name");
        events.add(new Event<>(oldName + " --> " + name, "NameChanged"));
        return events;
    }

    // @Override
    // Map<String, Object> getTrackedFields(){
    //     System.out.println("Hello");
    // }

}
