package by.toukach.walletservice.application.views;

/**
 * Класс для вывода формы о некорректности вводных данных в консоль.
 * */
public class UnknownViewChain extends ViewChain {

  public UnknownViewChain(ViewChain viewChain) {
    setNextViewChain(viewChain);
  }

  @Override
  public void handle() {
    System.out.println(ViewMessage.WRONG_OPTION);
  }
}