package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.UserDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Класс для вывода формы со списком действий в консоль.
 * */
public class AccountActionViewChain extends ViewChain {

  private final Map<Integer, ViewChain> viewChainMap = new HashMap<>();

  /**
   * Конструктор для создания формы со списком действий.
   *
   * @param userDto пользователь, который выполняет запрос.
   */
  public AccountActionViewChain(UserDto userDto) {
    setUserDto(userDto);
    viewChainMap.put(1, new CreateAccountViewChain(getUserDto()));
    viewChainMap.put(2, new ListAccountViewChain(getUserDto()));
    viewChainMap.put(3, new HistoryViewChain(getUserDto()));
    viewChainMap.put(4, this);
    viewChainMap.put(5, new WaitViewChain(userDto));
  }

  @Override
  public void handle() {
    System.out.println(ViewMessage.ACTION_LIST);
    Scanner scanner = getScanner();
    int answer = scanner.nextInt();
    scanner.nextLine();
    ViewChain viewChain = viewChainMap.get(answer);

    setNextViewChain(viewChain != null ? viewChain : new UnknownViewChain(this));
  }
}
