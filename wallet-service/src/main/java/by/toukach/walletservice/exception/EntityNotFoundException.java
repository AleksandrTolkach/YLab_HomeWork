package by.toukach.walletservice.exception;

/**
 * Класс представляющий исключение,
 * выбрасываемое в случае отсутствия запрашиваемой сущности в памяти.
 * */
public class EntityNotFoundException extends EntityException {

  public EntityNotFoundException() {
  }

  public EntityNotFoundException(String message) {
    super(message);
  }
}
