package by.toukach.walletservice.exception;

/**
 * Класс представляющий исключение, выбрасываемое при недостатке средств на счету.
 * */
public class InsufficientFundsException extends RuntimeException {

  public InsufficientFundsException(String message) {
    super(message);
  }
}
