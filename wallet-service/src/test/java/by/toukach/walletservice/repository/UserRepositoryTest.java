package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.ContainersEnvironment;
import by.toukach.walletservice.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest extends ContainersEnvironment {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private Migration migration;
  private User admin;
  private User newUser;
  private User createdUser;

  @BeforeEach
  public void setUp() throws NoSuchFieldException, IllegalAccessException {

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
