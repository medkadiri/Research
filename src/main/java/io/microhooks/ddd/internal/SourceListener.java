package io.microhooks.ddd.internal;

import java.lang.reflect.Field;

import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.springframework.beans.factory.annotation.Autowired;

import io.microhooks.ddd.Source;
import io.microhooks.eda.Event;
import io.microhooks.eda.providers.scs.EventProducer;
//import io.microhooks.eda.EventProducer;
import io.microhooks.util.Reflector;
import io.microhooks.util.logging.Logged;

public class SourceListener {

    @Autowired
    private EventProducer<Object> eventProducer;

    private static final String CREATED = "C";
    private static final String UPDATED = "U";
    private static final String DELETED = "D";

    @PostPersist
    @Logged
    public void onPostPersist(Object entity) throws Exception {
        Object key = getId(entity);
        Event<Object> event = new Event<>(entity, CREATED);
        eventProducer.publish(event, getSourceName(entity));
        System.out.println("---------------------------------------------" + getSourceName(entity));

        System.out.println("source listener");
        System.out.println("source entity name: " + entity.toString());
    }

    @PostUpdate
    @Logged
    public void onPostUpdate(Object entity) throws Exception {
        Object key = getId(entity);
        Event<Object> event = new Event<>(entity, CREATED);
        eventProducer.publish(event, getSourceName(entity));
        //eventProducer.publish(key, entity, UPDATED, getSourceName(entity));
    }

    @PostRemove
    @Logged
    public void onPostRemove(Object entity) throws Exception {
        Object key = getId(entity);
        Event<Object> event = new Event<>(entity, CREATED);
        eventProducer.publish(event, getSourceName(entity));
        //eventProducer.publish(key, null, DELETED, getSourceName(entity));
    }

    private Object getId(Object entity) throws Exception {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                return Reflector.getFieldValue(entity, field.getName());
            }
        }
        throw new IdNotFoundException();
    }

    private String getSourceName(Object entity) throws Exception {
        Source source = entity.getClass().<Source>getAnnotation(Source.class);
        if (source.name() != null && !source.name().trim().equals("")) {
            return source.name();
        }
        return entity.getClass().getName();
    }
}
