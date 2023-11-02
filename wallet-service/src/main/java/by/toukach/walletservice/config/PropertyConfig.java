package by.toukach.walletservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Класс для хранения конфигурационных параметров, указанных в application.yml.
 */
@Data
@Configuration
@PropertySource("classpath:application.yml")
public class PropertyConfig {

  @Value("${spring.datasource.url}")
  private String dataBaseUrl;

  @Value("${spring.datasource.dataBaseName}")
  private String dataBaseName;

  @Value("${spring.datasource.username}")
  private String dataBaseUsername;

  @Value("${spring.datasource.password}")
  private String dataBasePassword;

  @Value("${spring.datasource.driver}")
  private String dataBaseDriver;

  @Value("${spring.liquibase.changeLogFile}")
  private String liquibaseFile;

  @Value("${spring.liquibase.liquibaseSchema}")
  private String liquibaseScheme;
}
