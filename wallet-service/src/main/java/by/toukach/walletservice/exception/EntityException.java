package by.toukach.walletservice.exception;

/**
 * Класс представляющий исключение, выбрасываемое при работе над сущностями из БД.
 */
public class EntityException extends RuntimeException {

  public EntityException() {
  }

  public EntityException(String message) {
    super(message);
  }
}
