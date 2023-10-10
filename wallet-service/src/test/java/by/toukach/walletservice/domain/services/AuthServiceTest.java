package by.toukach.walletservice.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.models.LogInDto;
import by.toukach.walletservice.domain.models.SignUpDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.impl.AuthServiceImpl;
import by.toukach.walletservice.domain.services.impl.LoggerServiceImpl;
import by.toukach.walletservice.domain.services.impl.UserServiceImpl;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.infrastructure.entity.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceTest extends BaseTest {

  private AuthService authService;
  @Mock
  private UserServiceImpl userService;
  @Mock
  private LoggerServiceImpl loggerService;
  private MockedStatic<UserServiceImpl> userServiceMock;
  private MockedStatic<LoggerServiceImpl> loggerServiceMock;
  private LogInDto logIn;
  private SignUpDto signUp;
  private UserDto newUser;
  private UserDto createdUser;
  private Log newLog;
  private Log createdLog;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    logIn = getLogIn();
    signUp = getSignUp();
    newUser = getNewUserDto();
    createdUser = getCreatedUserDto();
    newLog = getNewLog();
    createdLog = getCreatedLog();

    userServiceMock = mockStatic(UserServiceImpl.class);
    userServiceMock.when(UserServiceImpl::getInstance).thenReturn(userService);

    loggerServiceMock = mockStatic(LoggerServiceImpl.class);
    loggerServiceMock.when(LoggerServiceImpl::getInstance).thenReturn(loggerService);

    Constructor<AuthServiceImpl> privateConstructor = AuthServiceImpl.class
        .getDeclaredConstructor();
    privateConstructor.setAccessible(true);

    authService = privateConstructor.newInstance();
  }

  @AfterEach
  public void cleanUp() {
    userServiceMock.close();
    loggerServiceMock.close();
  }

  @Test
  public void logInTest_should_SuccessfullyLogIn() {
    when(userService.findUserByLogin(LOGIN)).thenReturn(createdUser);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    UserDto expectedResult = createdUser;
    UserDto actualResult = authService.logIn(logIn);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void logInTest_should_ThrowError_WhenPasswordIncorrect() {
    logIn.setPassword(INCORRECT_PASSWORD);
    when(userService.findUserByLogin(LOGIN)).thenReturn(createdUser);

    assertThrows(EntityNotFoundException.class, () -> authService.logIn(logIn));
  }

  @Test
  public void logInTest_should_ThrowError_WhenLoginIncorrect() {
    when(userService.findUserByLogin(LOGIN)).thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class, () -> authService.logIn(logIn));
  }

  @Test
  public void signUpTest_should_SuccessfullySignUp() {
    when(userService.createUser(newUser)).thenReturn(createdUser);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    UserDto expectedResult = createdUser;
    UserDto actualResult = authService.signUp(signUp);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void signUpTest_should_ThrowError_WhenLoginDuplicate() {
    when(userService.createUser(newUser)).thenThrow(EntityDuplicateException.class);

    assertThrows(EntityDuplicateException.class, () -> authService.signUp(signUp));
  }
}
