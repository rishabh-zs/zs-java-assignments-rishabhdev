package com.zs.assignment5.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // Available at runtime for reflection
@Target(ElementType.TYPE)           // Applied to classes (The Service Layer)
public @interface ParserInfo {
    String version() default "1.0";
}