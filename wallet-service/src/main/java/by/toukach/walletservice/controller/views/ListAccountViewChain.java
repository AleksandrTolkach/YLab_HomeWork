package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import java.util.List;
import java.util.Scanner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода формы со списком счетов в консоль.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ListAccountViewChain extends AccountViewChain {

  public ListAccountViewChain(UserDto userDto) {
    setUserDto(userDto);
  }

  @Override
  public void handle() {

    List<AccountDto> accountDtos = getAccountService().findAccountsByUserId(getUserDto().getId());
    if (accountDtos.isEmpty()) {
      System.out.println(ViewMessage.ACCOUNTS_NOT_EXIST);
      setNextViewChain(new AccountActionViewChain(getUserDto()));
      return;
    }

    System.out.println(ViewMessage.ACCOUNT_CHOOSE);

    accountDtos.forEach(a -> System.out.println(String.format("%s. %s", a.getId(), a.getTitle())));

    Scanner scanner = getScanner();
    Long accountId = scanner.nextLong();
    scanner.nextLine();

    AccountDto accountDto = accountDtos.stream()
        .filter(a -> a.getId().equals(accountId))
        .findFirst()
        .orElse(null);

    if (accountDto == null) {
      setNextViewChain(new UnknownViewChain(this));
    } else {
      setNextViewChain(new SpecificAccountViewChain(getAccountService()
          .findAccountById(accountId), getUserDto()));
    }
  }
}
