package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.service.AuthService;
import by.toukach.walletservice.service.LoggerService;
import by.toukach.walletservice.service.UserService;
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
      UserDto userDto = userService.findUserByLogin(logInDto.getLogin());

      if (!userDto.getPassword().equals(logInDto.getPassword())) {
        throw new EntityNotFoundException();
      } else {

        Log log = LogUtil.prepareLog(LogType.AUTH, String.format(LogUtil.LOGIN, userDto.getId()));
        loggerService.createLog(log);

        return userDto;
      }
    } catch (EntityNotFoundException e) {
      throw new EntityNotFoundException(ExceptionMessage.WRONG_LOGIN_OR_PASSWORD);
    }
  }

  @Override
  public UserDto signUp(SignUpDto signUpDto) {
    UserDto userDto = UserDto.builder()
        .login(signUpDto.getLogin())
        .password(signUpDto.getPassword())
        .accountList(new ArrayList<>())
        .build();

    userDto = userService.createUser(userDto);

    Log log = LogUtil.prepareLog(LogType.AUTH, String.format(LogUtil.SIGN_UP, userDto.getId()));
    loggerService.createLog(log);

    return userDto;
  }

  public static AuthService getInstance() {
    return instance;
  }
}
