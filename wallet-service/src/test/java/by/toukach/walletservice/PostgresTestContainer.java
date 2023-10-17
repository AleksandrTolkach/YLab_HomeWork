package by.toukach.walletservice;

import by.toukach.walletservice.utils.param.ConfigParamProvider;
import by.toukach.walletservice.utils.param.ConfigParamProvider.ConfigParamVar;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

  private static final String IMAGE_VERSION = "postgres:14.2-alpine";
  private static final String INIT_SCRIPT_PATH = "db/create-scheme.sql";
  private static PostgreSQLContainer container;

  public PostgresTestContainer() {
    super(IMAGE_VERSION);
  }

  public static PostgreSQLContainer getInstance() {
    if (container == null) {
      container = new PostgreSQLContainer()
          .withDatabaseName(ConfigParamProvider.getParam(ConfigParamVar.DB_NAME))
          .withUsername(ConfigParamProvider.getParam(ConfigParamVar.DB_USERNAME))
          .withPassword(ConfigParamProvider.getParam(ConfigParamVar.DB_PASSWORD));
      container.withInitScript(INIT_SCRIPT_PATH);
      container.withExposedPorts(32999);
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
  }
}
