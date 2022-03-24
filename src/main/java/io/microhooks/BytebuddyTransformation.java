package io.microhooks;

import io.microhooks.ddd.Track;
import net.bytebuddy.build.Plugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Valuable;
import io.microhooks.ddd.Source;
import io.microhooks.ddd.internal.CustomListener;
import io.microhooks.ddd.internal.SourceListener;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

import javax.persistence.EntityListeners;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import org.atteo.classindex.ClassIndex;

import java.util.Map;


public class BytebuddyTransformation implements Plugin {

    @Override
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");

        //builder.defineField("trackedFields", Map<String, Object>, Modifier.PUBLIC);
        
        ByteBuddyAgent.install();

        //builder.implement(Track.class).

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
                    .implement(Track.class)
                    .annotateType(AnnotationDescription.Builder.ofType(EntityListeners.class)
                            .defineTypeArray("value", SourceListener.class,
                                    CustomListener.class)
                            .build())
                    .make()
                    .load(klass.getClassLoader(),
                            ClassReloadingStrategy.fromInstalledAgent());
        }
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean matches(TypeDescription target) {
        return false;
    }
}
