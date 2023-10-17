package by.toukach.walletservice;

import by.toukach.walletservice.utils.param.ConfigParamProvider;
import by.toukach.walletservice.utils.param.ConfigParamProvider.ConfigParamVar;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment extends BaseTest {

  @Container
  public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

  public void injectTestJdbcUrl() throws NoSuchFieldException, IllegalAccessException {
    Map<ConfigParamVar, String> params = new HashMap<>();
    Field paramsField = ConfigParamProvider.class.getDeclaredField("params");
    paramsField.setAccessible(true);

    params = (Map<ConfigParamVar, String>) paramsField.get(params);

    params.put(ConfigParamVar.DB_URL, postgreSQLContainer.getJdbcUrl());
  }
}
