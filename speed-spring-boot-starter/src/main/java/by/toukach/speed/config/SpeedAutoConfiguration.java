package by.toukach.speed.config;

import by.toukach.speed.aspect.MethodSpeedAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Конфигурационный файл для настройки модуля по замеру скорости выполнения методов.
 */
public class SpeedAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public MethodSpeedAspect methodSpeedAspect() {
    return new MethodSpeedAspect();
  }
}
