package by.toukach.walletservice.application.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Класс для вывода начальной формы в консоль.
 * */
public class EntryViewChain extends ViewChain {

  private final Map<Integer, ViewChain> viewChainMap = new HashMap<>();

  public EntryViewChain() {
    viewChainMap.put(1, new LogInViewChain());
    viewChainMap.put(2, new SignUpViewChain());
    viewChainMap.put(3, new WaitViewChain());
  }

  @Override
  public void handle() {
    System.out.println(ViewMessage.ENTRY);
    Scanner scanner = new Scanner(System.in);

    int answer = scanner.nextInt();
    scanner.nextLine();

    ViewChain viewChain = viewChainMap.get(answer);

    setNextViewChain(viewChain != null ? viewChain : new UnknownViewChain(this));
  }
}
