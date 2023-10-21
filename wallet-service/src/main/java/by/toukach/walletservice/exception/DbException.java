package by.toukach.walletservice.exception;

/**
 * Класс представляющий исключение,
 * выбрасываемое при выполнении соединения к базе, отправки запросов.
 * */
public class DbException extends RuntimeException {

  public DbException(String message) {
    super(message);
  }

  public DbException(String message, Throwable cause) {
    super(message, cause);
  }
}
