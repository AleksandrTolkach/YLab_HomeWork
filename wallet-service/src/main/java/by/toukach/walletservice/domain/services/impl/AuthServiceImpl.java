package by.toukach.walletservice.domain.services.impl;

import by.toukach.walletservice.domain.models.LogInDto;
import by.toukach.walletservice.domain.models.SignUpDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.AuthService;
import by.toukach.walletservice.domain.services.LoggerService;
import by.toukach.walletservice.domain.services.UserService;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import by.toukach.walletservice.infrastructure.entity.Log;
import by.toukach.walletservice.utils.LogUtil;
import java.util.ArrayList;

/**
 * Класс для аутентификации пользователей.
 * */
public class AuthServiceImpl implements AuthService {

  private static final AuthService instance = new AuthServiceImpl();

  private final UserService userService;
  private final LoggerService loggerService;

  private AuthServiceImpl() {
    userService = UserServiceImpl.getInstance();
    loggerService = LoggerServiceImpl.getInstance();
  }

  @Override
  public UserDto logIn(LogInDto logInDto) {
    try {
      UserDto user = userService.findUserByLogin(logInDto.getLogin());
      if (!user.getPassword().equals(logInDto.getPassword())) {
        throw new EntityNotFoundException();
      } else {

        Log log = LogUtil.prepareLog(LogType.AUTH, String.format(LogUtil.LOGIN, user.getId()));
        loggerService.createLog(log);

        return user;
      }
    } catch (EntityNotFoundException e) {
      throw new EntityNotFoundException(ExceptionMessage.WRONG_LOGIN_OR_PASSWORD);
    }
  }

  @Override
  public UserDto signUp(SignUpDto signUpDto) {
    UserDto user = UserDto.builder()
        .login(signUpDto.getLogin())
        .password(signUpDto.getPassword())
        .accountList(new ArrayList<>())
        .build();
    user = userService.createUser(user);

    Log log = LogUtil.prepareLog(LogType.AUTH, String.format(LogUtil.SIGN_UP, user.getId()));
    loggerService.createLog(log);

    return user;
  }

  public static AuthService getInstance() {
    return instance;
  }
}
