package by.toukach.speed.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import by.toukach.speed.config.SpeedAutoConfiguration;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * Аннотация, которая позволяет включить измерение скорости работы методов.
 */
@Retention(RUNTIME)
@Target(TYPE)
@Import(SpeedAutoConfiguration.class)
public @interface EnableMethodSpeedMeasurement {

}
