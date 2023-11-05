package by.toukach.walletservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment extends BaseTest {

  private static final String URL_PARAM_NAME = "spring.datasource.url";
  private static final String USERNAME_PARAM_NAME = "spring.datasource.username";
  private static final String PASSWORD_PARAM_NAME = "spring.datasource.password";
  private static final String DRIVER_CLASS_NAME_PARAM_NAME =
      "spring.datasource.driver-class-name";

  @Container
  public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

  @DynamicPropertySource
  public static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add(URL_PARAM_NAME, postgreSQLContainer::getJdbcUrl);
    dynamicPropertyRegistry.add(USERNAME_PARAM_NAME, postgreSQLContainer::getUsername);
    dynamicPropertyRegistry.add(PASSWORD_PARAM_NAME, postgreSQLContainer::getPassword);
    dynamicPropertyRegistry.add(DRIVER_CLASS_NAME_PARAM_NAME,
        postgreSQLContainer::getDriverClassName);
  }
}
