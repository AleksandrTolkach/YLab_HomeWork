package by.toukach.walletservice.application.views;

import by.toukach.walletservice.domain.models.LogInDto;
import by.toukach.walletservice.domain.models.LogInDto.LogInDtoBuilder;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.AuthService;
import by.toukach.walletservice.domain.services.impl.AuthServiceImpl;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import java.util.Scanner;

/**
 * Класс для вывода формы входа в консоль.
 * */
public class LogInViewChain extends ViewChain {

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

      if (userDto.getLogin().equals("admin")) {
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
