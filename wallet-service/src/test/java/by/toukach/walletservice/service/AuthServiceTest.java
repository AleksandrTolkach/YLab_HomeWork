package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.service.impl.AuthServiceImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.UserServiceImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("Тест входа в приложения")
  public void logInTest_should_SuccessfullyLogIn() {
    when(userService.findUserByLogin(LOGIN)).thenReturn(createdUser);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    UserDto expectedResult = createdUser;
    UserDto actualResult = authService.logIn(logIn);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест входа в приложение с некорректным паролем")
  public void logInTest_should_ThrowError_WhenPasswordIncorrect() {
    logIn.setPassword(INCORRECT_PASSWORD);
    when(userService.findUserByLogin(LOGIN)).thenReturn(createdUser);

    assertThatThrownBy(() -> authService.logIn(logIn))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест входа в приложения с несуществующим логином")
  public void logInTest_should_ThrowError_WhenLoginIncorrect() {
    when(userService.findUserByLogin(LOGIN)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> authService.logIn(logIn))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест регистрации в приложении")
  public void signUpTest_should_SuccessfullySignUp() {
    when(userService.createUser(newUser)).thenReturn(createdUser);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    UserDto expectedResult = createdUser;
    UserDto actualResult = authService.signUp(signUp);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест регистрации в приложении с дублирующим логином")
  public void signUpTest_should_ThrowError_WhenLoginDuplicate() {
    when(userService.createUser(newUser)).thenThrow(EntityDuplicateException.class);

    assertThatThrownBy(() -> authService.signUp(signUp))
        .isInstanceOf(EntityDuplicateException.class);
  }
}
