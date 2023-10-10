package by.toukach.walletservice.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.infrastructure.entity.UserEntity;
import by.toukach.walletservice.infrastructure.repositories.impl.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest extends BaseTest {

  @InjectMocks
  private UserRepositoryImpl userRepository;
  private UserEntity admin;
  private UserEntity newUser;
  private UserEntity createdUser;
  
  @BeforeEach
  public void setUp() {
    admin = getAdminEntity();
    newUser = getNewUserEntity();
    createdUser = getCreatedUserEntity();
  }

  @Test
  public void createUserTest_should_CreateUser() {
    UserEntity expectedResult = createdUser;
    UserEntity actualResult = userRepository.createUser(newUser);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findUserByIdTest_should_FindUser() {
    UserEntity expectedResult = admin;
    UserEntity actualResult = userRepository.findUserById(ADMIN_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findUserByIdTest_should_ReturnNullIfUserNotFound() {
    UserEntity expectedResult = null;
    UserEntity actualResult = userRepository.findUserById(UN_EXISTING_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findUserByLoginTest_should_FindUser() {
    UserEntity expectedResult = admin;
    UserEntity actualResult = userRepository.findUserByLogin(ADMIN_LOGIN);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findUserByLoginTest_should_ReturnNullIfUserNotFound() {
    UserEntity expectedResult = null;
    UserEntity actualResult = userRepository.findUserByLogin(UN_EXISTING_LOGIN);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void isExistsByIdTest_should_ReturnTrueIfUserExists() {
    boolean expectedResult = true;
    boolean actualResult = userRepository.isExists(ADMIN_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void isExistsByIdTest_should_ReturnFalseIfUserNotExist() {
    boolean expectedResult = false;
    boolean actualResult = userRepository.isExists(UN_EXISTING_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void isExistsByLoginTest_should_ReturnTrueIfUserExists() {
    boolean expectedResult = true;
    boolean actualResult = userRepository.isExists(ADMIN_LOGIN);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void isExistsByLoginTest_should_ReturnFalseIfUserNotExist() {
    boolean expectedResult = false;
    boolean actualResult = userRepository.isExists(UN_EXISTING_LOGIN);

    assertEquals(expectedResult, actualResult);
  }
}
