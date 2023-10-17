package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInDto.LogInDtoBuilder;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.service.AuthService;
import by.toukach.walletservice.service.impl.AuthServiceImpl;
import java.util.Scanner;

/**
 * Класс для вывода формы входа в консоль.
 * */
public class LogInViewChain extends ViewChain {

  private static final String ADMIN_LOGIN = "admin";

  private final AuthService authService;

  public LogInViewChain() {
    authService = AuthServiceImpl.getInstance();
  }

  @Override
  public void handle() {
    Scanner scanner = getScanner();

    LogInDtoBuilder logInDtoBuilder = LogInDto.builder();

    System.out.println(ViewMessage.LOGIN);
    logInDtoBuilder.login(scanner.nextLine());
    System.out.println(ViewMessage.PASSWORD);
    logInDtoBuilder.password(scanner.nextLine());

    try {
      UserDto userDto = authService.logIn(logInDtoBuilder.build());
      setUserDto(userDto);

      if (userDto.getLogin().equals(ADMIN_LOGIN)) {
        setNextViewChain(new AdminActionViewChain(userDto));
      } else {
        setNextViewChain(new AccountActionViewChain(getUserDto()));
      }
    } catch (EntityNotFoundException e) {
      System.out.println(ExceptionMessage.WRONG_LOGIN_OR_PASSWORD);
      setNextViewChain(new EntryViewChain());
    }
  }
}
