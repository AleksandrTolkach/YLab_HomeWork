package by.toukach.walletservice.exceptions;

/**
 * Клас представляющий исключение, выбрасываемое при некорректно переданных данных в метод.
 * */
public class ArgumentValueException extends RuntimeException {

  public ArgumentValueException(String message) {
    super(message);
  }
}
