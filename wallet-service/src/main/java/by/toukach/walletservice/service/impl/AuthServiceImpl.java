package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.aspect.annotation.Loggable;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInDtoResponse;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.enumiration.UserRole;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.security.Authentication;
import by.toukach.walletservice.security.AuthenticationManager;
import by.toukach.walletservice.security.impl.AuthenticationManagerImpl;
import by.toukach.walletservice.service.AuthService;
import by.toukach.walletservice.service.UserService;
import by.toukach.walletservice.validator.Validator;
import by.toukach.walletservice.validator.impl.LogInDtoValidator;
import by.toukach.walletservice.validator.impl.SignUpDtoValidator;
import java.util.ArrayList;

/**
 * Класс для аутентификации пользователей.
 */
public class AuthServiceImpl implements AuthService {

  private static final AuthService instance = new AuthServiceImpl();

  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final Validator<LogInDto> logInDtoValidator;
  private final Validator<SignUpDto> signUpDtoValidator;

  private AuthServiceImpl() {
    userService = UserServiceImpl.getInstance();
    authenticationManager = AuthenticationManagerImpl.getInstance();
    logInDtoValidator = LogInDtoValidator.getInstance();
    signUpDtoValidator = SignUpDtoValidator.getInstance();
  }

  @Override
  @Loggable(type = LogType.LOG_IN)
  public LogInDtoResponse logIn(LogInDto logInDto) {
    logInDtoValidator.validate(logInDto);

    try {
      UserDto userDto = userService.findUserByLogin(logInDto.getLogin());

      Authentication authentication = authenticationManager.authenticate(logInDto.getLogin(),
          logInDto.getPassword());

      if (!authentication.isAuthenticated()) {
        throw new EntityNotFoundException();
      } else {

        return LogInDtoResponse.builder()
            .authentication(authentication)
            .userDto(userDto)
            .build();
      }
    } catch (EntityNotFoundException e) {
      throw new EntityNotFoundException(ExceptionMessage.WRONG_LOGIN_OR_PASSWORD);
    }
  }

  @Override
  @Loggable(type = LogType.SIGN_UP)
  public LogInDtoResponse signUp(SignUpDto signUpDto) {
    signUpDtoValidator.validate(signUpDto);

    String login = signUpDto.getLogin();
    String password = signUpDto.getPassword();

    UserDto userDto = UserDto.builder()
        .login(login)
        .password(password)
        .role(UserRole.USER)
        .accountList(new ArrayList<>())
        .build();

    userDto = userService.createUser(userDto);

    Authentication authentication = authenticationManager.authenticate(login, password);

    return LogInDtoResponse.builder()
        .authentication(authentication)
        .userDto(userDto)
        .build();
  }

  @Override
  @Loggable(type = LogType.LOG_OUT)
  public LogInDtoResponse logOut(String login) {
    UserDto userDto = userService.findUserByLogin(login);
    authenticationManager.clearAuthentication(login);
    return LogInDtoResponse.builder()
        .userDto(userDto)
        .build();
  }

  public static AuthService getInstance() {
    return instance;
  }


}
