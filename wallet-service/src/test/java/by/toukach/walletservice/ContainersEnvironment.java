package by.toukach.walletservice;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment extends BaseTest {

  @Container
  public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();
}
