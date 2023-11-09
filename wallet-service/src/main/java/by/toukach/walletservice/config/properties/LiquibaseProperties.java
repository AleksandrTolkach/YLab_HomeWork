package by.toukach.walletservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Класс для хранения параметров для liquibase.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.liquibase")
@Data
public class LiquibaseProperties {

  private String changeLog;
  private String liquibaseSchema;
  private boolean enabled;
}
