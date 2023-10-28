package by.toukach.walletservice.config;

import by.toukach.walletservice.aspect.LoggableAspect;
import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
  
  @Bean
  public LoggableAspect loggableAspect() {
    return Aspects.aspectOf(LoggableAspect.class);
  }
}
