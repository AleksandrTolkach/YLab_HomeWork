package by.toukach.walletservice.utils.param;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Утилитарный класс предоставляющий доступ к конфигурационным параметрам для базы данных.
 */
public class ConfigParamProvider {

  private static final String CONFIGURATION_FILE_PATH = "application.yml";
  private static final Map<ConfigParamVar, String> params = new HashMap<>();

  static {
    InputStream resourceAsStream = ConfigParamProvider.class.getClassLoader()
        .getResourceAsStream(CONFIGURATION_FILE_PATH);

    Yaml configFile = new Yaml(new Constructor(WalletServiceParam.class));
    WalletServiceParam walletServiceParam = configFile.load(resourceAsStream);

    params.put(ConfigParamVar.DB_URL, walletServiceParam.getDatabase().getUrl());
    params.put(ConfigParamVar.DB_NAME, walletServiceParam.getDatabase().getName());
    params.put(ConfigParamVar.DB_USERNAME, walletServiceParam.getDatabase().getUsername());
    params.put(ConfigParamVar.DB_PASSWORD, walletServiceParam.getDatabase().getPassword());
    params.put(ConfigParamVar.DB_DRIVER, walletServiceParam.getDatabase().getDriver());
    params.put(ConfigParamVar.DB_CHANGE_LOG_FILE,
        walletServiceParam.getDatabase().getChangeLogFile());
    params.put(ConfigParamVar.DB_LIQUIBASE_SCHEMA,
        walletServiceParam.getDatabase().getLiquibaseSchema());
  }

  /**
   * Метод для получения значения параметра из конфигурационного файла.
   *
   * @param configParamVar запрашиваемый параметр.
   * @return значение искомого параметра.
   */
  public static String getParam(ConfigParamVar configParamVar) {
    return params.get(configParamVar);
  }

  /**
   * Перечисление параметров в конфигурационном файле.
   */
  public enum ConfigParamVar {
    DB_URL, DB_NAME, DB_USERNAME, DB_PASSWORD, DB_DRIVER, DB_CHANGE_LOG_FILE, DB_LIQUIBASE_SCHEMA
  }
}
