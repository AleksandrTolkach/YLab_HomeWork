package by.toukach.walletservice.application.views;

import by.toukach.walletservice.domain.models.SignUpDto;
import by.toukach.walletservice.domain.models.SignUpDto.SignUpDtoBuilder;
import by.toukach.walletservice.domain.services.AuthService;
import by.toukach.walletservice.domain.services.impl.AuthServiceImpl;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
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
