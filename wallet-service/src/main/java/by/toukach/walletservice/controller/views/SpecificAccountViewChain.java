package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода формы с данными о счете в консоль.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecificAccountViewChain extends AccountViewChain {

  private final Map<Integer, ViewChain> viewChainMap = new HashMap<>();

  public SpecificAccountViewChain(AccountDto accountDto, UserDto userDto) {
    setAccountDto(accountDto);
    viewChainMap.put(1, new CreditAccountViewChain(accountDto, userDto));
    viewChainMap.put(2, new DebitAccountViewChain(accountDto, userDto));
    viewChainMap.put(3, new AccountActionViewChain(userDto));
    viewChainMap.put(4, new WaitViewChain(userDto));
  }

  @Override
  public void handle() {
    System.out.println(String.format(ViewMessage.USER_DETAIL,
        getAccountDto().getId(), getAccountDto().getTitle(), getAccountDto().getSum()));
    System.out.println(ViewMessage.ACCOUNT_ACTION);

    Scanner scanner = getScanner();
    int answer = scanner.nextInt();
    scanner.nextLine();

    ViewChain viewChain = viewChainMap.get(answer);

    setNextViewChain(viewChain != null ? viewChain : new UnknownViewChain(this));
  }
}
