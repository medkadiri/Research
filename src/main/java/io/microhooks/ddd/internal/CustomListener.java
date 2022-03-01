package io.microhooks.ddd.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.springframework.beans.factory.annotation.Autowired;

import io.microhooks.ddd.OnCreate;
import io.microhooks.ddd.OnDelete;
import io.microhooks.ddd.OnUpdate;
import io.microhooks.ddd.Track;
import io.microhooks.eda.Event;
import io.microhooks.eda.providers.scs.EventProducer;
import io.microhooks.util.Reflector;
import io.microhooks.util.logging.Logged;

public class CustomListener {

    @Autowired
    private EventProducer<Object> eventProducer;

    @PostPersist
    @Logged
    @SuppressWarnings("unchecked")
    public void onPostPersist(Object entity) throws Exception {
        Map<Long, String> keyMap = new HashMap<>();
        keyMap.put((Long) getId(entity), entity.getClass().getName());
        ObjectsRegistry.putMap(keyMap, new HashMap<String, Object>());
        setTrackedFields(entity);
        for (Method method : entity.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(OnCreate.class)) {
                List<Event<Object>> events = (List<Event<Object>>) method.invoke(entity);
                Object key = getId(entity);
                publish(key, events, method.getAnnotation(OnCreate.class).streams());
                // Don't return here as we allow several methods to be annotated with OnCreate
            }
        }
    }

    @PostUpdate
    @Logged
    @SuppressWarnings("unchecked")
    public void onPostUpdate(Object entity) throws Exception {
        for (Method method : entity.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(OnUpdate.class)) {
                Map<Long, String> keyMap = new HashMap<>();
                keyMap.put((Long) getId(entity), entity.getClass().getName());
                Map<String, Object> trackedFields = ObjectsRegistry.getMap(keyMap);
                Iterator<String> keys = trackedFields.keySet().iterator();
                Map<String, Object> changedTrackedFields = new HashMap<>();
                while (keys.hasNext()) {
                    String fieldName = keys.next();
                    Object oldValue = trackedFields.get(fieldName);
                    Object newValue = Reflector.getFieldValue(entity, fieldName);
                    if (oldValue == null && newValue == null) {
                        trackedFields.put(fieldName, newValue);
                    }
                }
                List<Event<Object>> events = (List<Event<Object>>) method.invoke(entity,
                        changedTrackedFields);
                Object key = getId(entity);
                publish(key, events, method.getAnnotation(OnUpdate.class).streams());
                // Don't return here as we allow several methods to be annotated with OnUpdate
            }
        }
    }

    @PostLoad
    @Logged
    public void onPostLoad(Object entity) throws Exception {
        setTrackedFields(entity);
    }

    @PostRemove
    @Logged
    @SuppressWarnings("unchecked")
    public void onPostRemove(Object entity) throws Exception {
        for (Method method : entity.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(OnDelete.class)) {
                List<Event<Object>> events = (List<Event<Object>>) method.invoke(entity);
                Object key = getId(entity);
                publish(key, events, method.getAnnotation(OnDelete.class).streams());
                // Don't return here as we allow several methods to be annotated with OnDelete
            }
        }
    }

    private void publish(Object key, List<Event<Object>> events, String[] streams) {
        System.out.println(events);
        System.out.println(eventProducer.getClass());
        try {
            events.forEach(event -> eventProducer.publish(
                    events.get(0), streams[0]));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void setTrackedFields(Object entity) throws Exception {
        Field[] fields = entity.getClass().getDeclaredFields();
        Map<Long, String> keyMap = new HashMap<>();
        keyMap.put((Long) getId(entity), entity.getClass().getName());
        Map<String, Object> trackedFields = ObjectsRegistry.getMap(keyMap);
        for (Field field : fields) {
            if (field.isAnnotationPresent(Track.class)) {
                Object fieldValue = Reflector.getFieldValue(entity, field.getName());
                trackedFields.put(field.getName(), fieldValue);
            }
        }
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

}
