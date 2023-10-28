package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.toukach.walletservice.ContainersEnvironment;
import by.toukach.walletservice.PostgresTestContainer;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.repository.impl.MigrationImpl;
import by.toukach.walletservice.repository.impl.UserRepositoryImpl;
import java.util.Optional;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class UserRepositoryTest extends ContainersEnvironment {

  private UserRepository userRepository;
  private Migration migration;
  private User admin;
  private User newUser;
  private User createdUser;

  @Rule
  public PostgreSQLContainer postgresContainer = PostgresTestContainer.getInstance();
  
  @BeforeEach
  public void setUp() throws NoSuchFieldException, IllegalAccessException {
    injectTestJdbcUrl();

    migration = MigrationImpl.getInstance();
    migration.migrate();

    userRepository = UserRepositoryImpl.getInstance();
    admin = getAdmin();
    newUser = getNewUserWithRole();
    createdUser = getCreatedUser();
  }

  @AfterEach
  public void cleanUp() {
    migration.rollback(TAG_V_0_0);
  }

  @Test
  @DisplayName("Тест сохранения пользователя в БД")
  public void createUserTest_should_CreateUser() {
    User expectedResult = createdUser;
    User actualResult = userRepository.createUser(newUser);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя в БД по ID")
  public void findUserByIdTest_should_FindUser() {
    Optional<User> expectedResult = Optional.of(admin);
    Optional<User> actualResult = userRepository.findUserById(ADMIN_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя в БД по несуществующему ID")
  public void findUserByIdTest_should_ReturnEmptyOptional() {
    Optional<User> expectedResult = Optional.empty();
    Optional<User> actualResult = userRepository.findUserById(UN_EXISTING_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя по логину")
  public void findUserByLoginTest_should_FindUser() {
    Optional<User> expectedResult = Optional.of(admin);
    Optional<User> actualResult = userRepository.findUserByLogin(ADMIN_LOGIN);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя по несуществующему логину")
  public void findUserByLoginTest_should_ReturnEmptyOptional() {
    Optional<User> expectedResult = Optional.empty();
    Optional<User> actualResult = userRepository.findUserByLogin(UN_EXISTING_LOGIN);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест проверки существования пользователя по ID")
  public void isExistsByIdTest_should_ReturnTrueIfUserExists() {
    boolean expectedResult = true;
    boolean actualResult = userRepository.isExists(ADMIN_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест проверки существования пользователя по несуществующему ID")
  public void isExistsByIdTest_should_ReturnFalseIfUserNotExist() {
    boolean expectedResult = false;
    boolean actualResult = userRepository.isExists(UN_EXISTING_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест проверки существования пользователя по логину")
  public void isExistsByLoginTest_should_ReturnTrueIfUserExists() {
    boolean expectedResult = true;
    boolean actualResult = userRepository.isExists(ADMIN_LOGIN);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест проверки существования пользователя по несуществующему логину")
  public void isExistsByLoginTest_should_ReturnFalseIfUserNotExist() {
    boolean expectedResult = false;
    boolean actualResult = userRepository.isExists(UN_EXISTING_LOGIN);

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
