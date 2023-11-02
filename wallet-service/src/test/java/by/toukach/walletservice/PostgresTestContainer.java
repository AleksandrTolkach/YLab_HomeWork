package by.toukach.walletservice;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

  private static final String IMAGE_VERSION = "postgres:14.2-alpine";
  private static final String INIT_SCRIPT_PATH = "db/create-scheme.sql";
  private static final String DB_NAME = "wallet";
  private static final String DB_USERNAME = "toukach";
  private static final String DB_PASSWORD = "ylab";
  private static PostgreSQLContainer container;

  public PostgresTestContainer() {
    super(IMAGE_VERSION);
  }

  public static PostgreSQLContainer getInstance() {
    if (container == null) {
      container = new PostgreSQLContainer()
          .withDatabaseName(DB_NAME)
          .withUsername(DB_USERNAME)
          .withPassword(DB_PASSWORD);
      container.withInitScript(INIT_SCRIPT_PATH);
    }
    return container;
  }

  @Override
  public void start() {
    super.start();
  }
}
