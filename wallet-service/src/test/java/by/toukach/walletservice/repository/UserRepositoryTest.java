package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest extends BaseTest {

  @InjectMocks
  private UserRepositoryImpl userRepository;
  private User admin;
  private User newUser;
  private User createdUser;
  
  @BeforeEach
  public void setUp() {
    admin = getAdminEntity();
    newUser = getNewUserEntity();
    createdUser = getCreatedUserEntity();
  }

  @Test
  @DisplayName("Тест сохранения пользователя в памяти")
  public void createUserTest_should_CreateUser() {
    User expectedResult = createdUser;
    User actualResult = userRepository.createUser(newUser);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя в памяти по ID")
  public void findUserByIdTest_should_FindUser() {
    User expectedResult = admin;
    User actualResult = userRepository.findUserById(ADMIN_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя в памяти по несуществующему ID")
  public void findUserByIdTest_should_ReturnNullIfUserNotFound() {
    User expectedResult = null;
    User actualResult = userRepository.findUserById(UN_EXISTING_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя по логину")
  public void findUserByLoginTest_should_FindUser() {
    User expectedResult = admin;
    User actualResult = userRepository.findUserByLogin(ADMIN_LOGIN);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя по несуществующему логину")
  public void findUserByLoginTest_should_ReturnNullIfUserNotFound() {
    User expectedResult = null;
    User actualResult = userRepository.findUserByLogin(UN_EXISTING_LOGIN);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест проверки существования пользователя по ID")
  public void isExistsByIdTest_should_ReturnTrueIfUserExists() {
    boolean expectedResult = true;
    boolean actualResult = userRepository.isExists(ADMIN_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест проверки существования пользователя по несуществующему ID")
  public void isExistsByIdTest_should_ReturnFalseIfUserNotExist() {
    boolean expectedResult = false;
    boolean actualResult = userRepository.isExists(UN_EXISTING_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест проверки существования пользователя по логину")
  public void isExistsByLoginTest_should_ReturnTrueIfUserExists() {
    boolean expectedResult = true;
    boolean actualResult = userRepository.isExists(ADMIN_LOGIN);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест проверки существования пользователя по несуществующему логину")
  public void isExistsByLoginTest_should_ReturnFalseIfUserNotExist() {
    boolean expectedResult = false;
    boolean actualResult = userRepository.isExists(UN_EXISTING_LOGIN);

    assertThat(expectedResult).isEqualTo(actualResult);
  }
}
