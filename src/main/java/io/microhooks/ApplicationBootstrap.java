package io.microhooks;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

import io.microhooks.ddd.Source;
import io.microhooks.ddd.internal.CustomListener;
import io.microhooks.ddd.internal.SourceListener;

import java.util.Iterator;

import javax.persistence.EntityListeners;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import org.atteo.classindex.ClassIndex;

public class ApplicationBootstrap implements ApplicationListener<ApplicationPreparedEvent> {

        public void onApplicationEvent(ApplicationPreparedEvent ev) {
               System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");

               ByteBuddyAgent.install();

               Iterable<Class<?>> sourceClasses = ClassIndex.getAnnotated(Source.class);

               // lookup entity classes with @source
               ByteBuddy byteBuddy = new ByteBuddy();
               Iterator<Class<?>> iterator = sourceClasses.iterator();

               if (iterator == null)
                       System.out.println("Iterator is null");

               while (iterator.hasNext()) {
                       Class<?> klass = iterator.next();
                       System.out.println(klass.toString());

                       byteBuddy
                                       .redefine(klass)
                                       .annotateType(AnnotationDescription.Builder.ofType(EntityListeners.class)
                                                       .defineTypeArray("value", SourceListener.class,
                                                                       CustomListener.class)
                                                       .build())
                                       .make()
                                       .load(klass.getClassLoader(),
                                                       ClassReloadingStrategy.fromInstalledAgent());
               }
        }
}
