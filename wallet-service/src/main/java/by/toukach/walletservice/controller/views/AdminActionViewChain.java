package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.UserDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода формы со списком действий для администратора в консоль.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminActionViewChain extends ViewChain {

  private final Map<Integer, ViewChain> viewChainMap = new HashMap<>();

  public AdminActionViewChain(UserDto userDto) {
    setUserDto(userDto);

    viewChainMap.put(1, new LoggerViewChain(userDto));
    viewChainMap.put(2, new WaitViewChain(userDto));
  }

  @Override
  public void handle() {
    System.out.println(ViewMessage.ADMIN_ACTION_LIST);
    Scanner scanner = getScanner();
    int answer = scanner.nextInt();
    scanner.nextLine();
    ViewChain viewChain = viewChainMap.get(answer);

    setNextViewChain(viewChain != null ? viewChain : new UnknownViewChain(this));
  }
}
