package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInResponseDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.enumiration.UserRole;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.service.auth.impl.AuthServiceImpl;
import by.toukach.walletservice.service.user.UserService;
import by.toukach.walletservice.validator.Validator;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceTest extends BaseTest {

  @InjectMocks
  private AuthServiceImpl authService;
  @Mock
  private UserService userService;
  @Mock
  private Validator<LogInDto> logInDtoValidator;
  @Mock
  private Validator<SignUpDto> signUpDtoValidator;
  @Mock
  private AuthenticationManager authenticationManager;
  private LogInDto logInDto;
  private SignUpDto signUp;
  private UserDto newUser;
  private UserDto createdUser;
  private LogInResponseDto logInResponseDto;
  private Authentication authentication;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    logInDto = getLogIn();
    signUp = getSignUp();
    newUser = getNewUserDto();
    createdUser = getCreatedUserDto();
    logInResponseDto = getLogInDtoResponse();
    authentication = getAuthentication();
  }

  @Test
  @DisplayName("Тест входа в приложения")
  public void logInTest_should_SuccessfullyLogIn() {
    doNothing().when(logInDtoValidator).validate(logInDto);
    when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
    when(userService.findUserByLogin(LOGIN)).thenReturn(createdUser);

    LogInResponseDto expectedResult = logInResponseDto;
    LogInResponseDto actualResult = authService.logIn(logInDto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест входа в приложения с несуществующим логином")
  public void logInTest_should_ThrowError_WhenLoginIncorrect() {
    when(userService.findUserByLogin(LOGIN)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> authService.logIn(logInDto))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест регистрации в приложении")
  public void signUpTest_should_SuccessfullySignUp() {
    doNothing().when(signUpDtoValidator).validate(signUp);
    newUser.setRole(UserRole.USER);
    when(userService.createUser(newUser)).thenReturn(createdUser);
    when(authenticationManager.authenticate(authentication)).thenReturn(authentication);

    LogInResponseDto expectedResult = logInResponseDto;
    LogInResponseDto actualResult = authService.signUp(signUp);

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
