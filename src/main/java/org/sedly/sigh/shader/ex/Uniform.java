package org.sedly.sigh.shader.ex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Uniform {

  enum Type {
    LEAF, NODE
  }

  Type type();

  String name() default "";

}
