package io.microhooks.ddd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.atteo.classindex.IndexAnnotated;

@IndexAnnotated //class indexing
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Source {
    String name() default "";
}
