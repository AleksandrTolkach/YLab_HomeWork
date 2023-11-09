package by.toukach.walletservice.config.properties;

import javax.sql.DataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Класс для хранения параметров БД.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DbProperties {

  private String url;
  private String dbName;
  private String username;
  private String password;
  private String driverClassName;
  private String defaultDbUrl;
  private final DataSource dataSource;
}
