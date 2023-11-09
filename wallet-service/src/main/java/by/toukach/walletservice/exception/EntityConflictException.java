package by.toukach.walletservice.exception;


/**
 * Класс представляющий исключение, выбрасываемое в случаях,
 * когда пользователь пытается работать от имени другого пользователя.
 */
public class EntityConflictException extends EntityException {

  public EntityConflictException(String message) {
    super(message);
  }
}
