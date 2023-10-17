package by.toukach.walletservice.utils.param;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс отражающий параметры базы данных в конфигурационном файле.
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBaseParam {
  private String url;
  private String name;
  private String username;
  private String password;
  private String driver;
  private String changeLogFile;
  private String liquibaseSchema;
}
