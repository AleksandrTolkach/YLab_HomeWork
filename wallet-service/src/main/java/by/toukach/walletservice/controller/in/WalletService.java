package by.toukach.walletservice.controller.in;

import by.toukach.walletservice.controller.views.EntryViewChain;
import by.toukach.walletservice.controller.views.ViewChain;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import java.util.InputMismatchException;
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
    ViewChain viewChain = new EntryViewChain();

    while (true) {
      try {
        viewChain.handle();
      } catch (InputMismatchException e) {
        System.out.println(ExceptionMessage.WRONG_SYMBOL_MESSAGE);
        viewChain.setScanner(new Scanner(System.in));
        viewChain.setNextViewChain(viewChain);
      }
      viewChain = viewChain.getNextViewChain();
    }
  }
}
