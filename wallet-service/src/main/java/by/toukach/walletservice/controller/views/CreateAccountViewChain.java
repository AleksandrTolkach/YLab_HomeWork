package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import java.util.Scanner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода формы по созданию счета в консоль.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class CreateAccountViewChain extends AccountViewChain {

  public CreateAccountViewChain(UserDto userDto) {
    setUserDto(userDto);
  }

  @Override
  public void handle() {
    System.out.println(ViewMessage.ACCOUNT_NUMBER);

    Scanner scanner = getScanner();
    String title = scanner.nextLine();

    AccountDto accountDto = AccountDto.builder()
        .title(title)
        .sum(0.0)
        .userId(getUserDto().getId())
        .build();

    accountDto = getAccountService().createAccount(accountDto);

    setNextViewChain(new SpecificAccountViewChain(accountDto, getUserDto()));
  }
}
