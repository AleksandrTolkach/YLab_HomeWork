package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.SignUpDto.SignUpDtoBuilder;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.service.AuthService;
import by.toukach.walletservice.service.impl.AuthServiceImpl;
import java.util.Scanner;

/**
 * Класс для вывода формы регистрации в консоль.
 * */
public class SignUpViewChain extends ViewChain {

  private final AuthService authService;

  public SignUpViewChain() {
    authService = AuthServiceImpl.getInstance();
  }

  @Override
  public void handle() {
    Scanner scanner = getScanner();
    SignUpDtoBuilder signUpDtoBuilder = SignUpDto.builder();

    System.out.println(ViewMessage.LOGIN);
    signUpDtoBuilder.login(scanner.nextLine());
    System.out.println(ViewMessage.PASSWORD);
    signUpDtoBuilder.password(scanner.nextLine());

    try {
      setUserDto(authService.signUp(signUpDtoBuilder.build()));
      setNextViewChain(new AccountActionViewChain(getUserDto()));
    } catch (EntityDuplicateException e) {
      System.out.println(e.getMessage());
      setNextViewChain(new EntryViewChain());
    }
  }
}
