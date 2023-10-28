package by.toukach.walletservice.aspect.annotation;

import by.toukach.walletservice.enumiration.LogType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для указания метода, в котором необходимо выполнить логирование.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Loggable {

  /**
   * Метод для получения типа логирования.
   *
   * @return тип логирования.
   */
  LogType type();
}
