package by.toukach.walletservice.config;

import by.toukach.walletservice.aspect.LoggableAspect;
import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Конфигурационный клас для настройки аспектов в Spring приложении.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectConfig {

  /**
   * Метод для создания бина из LoggableAspect.
   *
   * @return запрашиваемый LoggableAspect.
   */
  @Bean
  public LoggableAspect loggableAspect() {
    return Aspects.aspectOf(LoggableAspect.class);
  }
}
