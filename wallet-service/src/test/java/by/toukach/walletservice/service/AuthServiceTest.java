package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInDtoResponse;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.enumiration.UserRole;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.security.Authentication;
import by.toukach.walletservice.security.impl.AuthenticationManagerImpl;
import by.toukach.walletservice.service.impl.AuthServiceImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.UserServiceImpl;
import by.toukach.walletservice.validator.impl.LogInDtoValidator;
import by.toukach.walletservice.validator.impl.SignUpDtoValidator;
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
  private LogInDtoValidator logInDtoValidator;
  @Mock
  private SignUpDtoValidator signUpDtoValidator;
  @Mock
  private AuthenticationManagerImpl authenticationManager;
  @Mock
  private LoggerServiceImpl loggerService;
  private MockedStatic<UserServiceImpl> userServiceMock;
  private MockedStatic<LogInDtoValidator> logInDtoValidatorMock;
  private MockedStatic<SignUpDtoValidator> signUpDtoValidatorMock;
  private MockedStatic<AuthenticationManagerImpl> authenticationManagerMock;
  private MockedStatic<LoggerServiceImpl> loggerServiceMock;
  private LogInDto logIn;
  private SignUpDto signUp;
  private UserDto newUser;
  private UserDto createdUser;
  private Authentication successfulAuthentication;
  private Authentication unSuccessfulAuthentication;
  private LogInDtoResponse logInDtoResponse;
  private Log log;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    logIn = getLogIn();
    signUp = getSignUp();
    newUser = getNewUserDto();
    createdUser = getCreatedUserDto();
    successfulAuthentication = getSuccessfulAuthentication();
    unSuccessfulAuthentication = getUnSuccessfulAuthentication();
    logInDtoResponse = getLogInDtoResponse();
    log = getCreatedLog();

    userServiceMock = mockStatic(UserServiceImpl.class);
    userServiceMock.when(UserServiceImpl::getInstance).thenReturn(userService);

    logInDtoValidatorMock = mockStatic(LogInDtoValidator.class);
    logInDtoValidatorMock.when(LogInDtoValidator::getInstance).thenReturn(logInDtoValidator);

    signUpDtoValidatorMock = mockStatic(SignUpDtoValidator.class);
    signUpDtoValidatorMock.when(SignUpDtoValidator::getInstance).thenReturn(signUpDtoValidator);

    authenticationManagerMock = mockStatic(AuthenticationManagerImpl.class);
    authenticationManagerMock.when(AuthenticationManagerImpl::getInstance)
        .thenReturn(authenticationManager);

    loggerServiceMock = mockStatic(LoggerServiceImpl.class);
    logInDtoValidatorMock.when(LoggerServiceImpl::getInstance).thenReturn(loggerService);

    Constructor<AuthServiceImpl> privateConstructor = AuthServiceImpl.class
        .getDeclaredConstructor();
    privateConstructor.setAccessible(true);

    authService = privateConstructor.newInstance();
  }

  @AfterEach
  public void cleanUp() {
    userServiceMock.close();
    logInDtoValidatorMock.close();
    signUpDtoValidatorMock.close();
    authenticationManagerMock.close();
    loggerServiceMock.close();
  }

  @Test
  @DisplayName("Тест входа в приложения")
  public void logInTest_should_SuccessfullyLogIn() {
    doNothing().when(logInDtoValidator).validate(logIn);
    when(userService.findUserByLogin(LOGIN)).thenReturn(createdUser);
    when(authenticationManager.authenticate(LOGIN, PASSWORD)).thenReturn(successfulAuthentication);
    when(loggerService.createLog(any())).thenReturn(log);

    LogInDtoResponse expectedResult = logInDtoResponse;
    LogInDtoResponse actualResult = authService.logIn(logIn);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест входа в приложение с некорректным паролем")
  public void logInTest_should_ThrowError_WhenPasswordIncorrect() {
    logIn.setPassword(INCORRECT_PASSWORD);
    when(userService.findUserByLogin(LOGIN)).thenReturn(createdUser);
    when(authenticationManager.authenticate(LOGIN, INCORRECT_PASSWORD))
        .thenReturn(unSuccessfulAuthentication);

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
    doNothing().when(signUpDtoValidator).validate(signUp);
    newUser.setRole(UserRole.USER);
    when(userService.createUser(newUser)).thenReturn(createdUser);
    when(authenticationManager.authenticate(LOGIN, PASSWORD)).thenReturn(successfulAuthentication);
    when(loggerService.createLog(any())).thenReturn(log);

    LogInDtoResponse expectedResult = logInDtoResponse;
    LogInDtoResponse actualResult = authService.signUp(signUp);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест регистрации в приложении с дублирующим логином")
  public void signUpTest_should_ThrowError_WhenLoginDuplicate() {
    doNothing().when(signUpDtoValidator).validate(signUp);
    newUser.setRole(UserRole.USER);
    when(userService.createUser(newUser)).thenThrow(EntityDuplicateException.class);

    assertThatThrownBy(() -> authService.signUp(signUp))
        .isInstanceOf(EntityDuplicateException.class);
  }
}
