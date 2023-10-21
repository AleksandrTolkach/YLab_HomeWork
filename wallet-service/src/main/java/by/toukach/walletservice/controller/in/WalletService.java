package by.toukach.walletservice.controller.in;

import by.toukach.walletservice.controller.views.EntryViewChain;
import by.toukach.walletservice.controller.views.ExitViewChain;
import by.toukach.walletservice.controller.views.ViewChain;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.Migration;
import by.toukach.walletservice.repository.impl.MigrationImpl;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Головной класс для запуска проекта.
 * */
public class WalletService {

  /**
   * Точка входа для выполнения программы.
   *
   * @param args аргументы.
   */
  public static void main(String[] args) {
    Migration migration = MigrationImpl.getInstance();
    migration.migrate();

    ViewChain viewChain = new EntryViewChain();

    while (true) {
      try {
        viewChain.handle();
      } catch (NoSuchElementException e) {
        System.out.println(ExceptionMessage.WRONG_SYMBOL_MESSAGE);
        viewChain.setScanner(new Scanner(System.in));
        viewChain.setNextViewChain(viewChain);
      }
      viewChain = viewChain.getNextViewChain();
      if (viewChain instanceof ExitViewChain) {
        viewChain.handle();
        return;
      }
    }
  }
}
