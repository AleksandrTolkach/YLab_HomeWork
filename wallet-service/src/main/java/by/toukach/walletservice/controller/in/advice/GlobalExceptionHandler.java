package by.toukach.walletservice.controller.in.advice;

import by.toukach.walletservice.exception.ArgumentValueException;
import by.toukach.walletservice.exception.BaseError;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.EntityConflictException;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.InsufficientFundsException;
import by.toukach.walletservice.exception.ValidationExceptionList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Класс для перехвата исключений. выбрасываемых приложением..
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Метод для перехвата ArgumentValueException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(ArgumentValueException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseError handleArgumentValueException(ArgumentValueException e) {
    return new BaseError(e.getMessage());
  }

  /**
   * Метод для перехвата DbException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(DbException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public BaseError handleDbException(DbException e) {
    return new BaseError(e.getMessage());
  }

  /**
   * Метод для перехвата EntityConflictException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(EntityConflictException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public BaseError handleEntityConflictException(EntityConflictException e) {
    return new BaseError(e.getMessage());
  }

  /**
   * Метод для перехвата EntityDuplicateException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(EntityDuplicateException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public BaseError handleEntityDuplicateException(EntityDuplicateException e) {
    return new BaseError(e.getMessage());
  }

  /**
   * Метод для перехвата EntityNotFoundException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public BaseError handleEntityNotFoundException(EntityNotFoundException e) {
    return new BaseError(e.getMessage());
  }

  /**
   * Метод для перехвата InsufficientFundsException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(InsufficientFundsException.class)
  @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
  public BaseError handleInsufficientFundsException(InsufficientFundsException e) {
    return new BaseError(e.getMessage());
  }

  /**
   * Метод для перехвата TypeMismatchException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(TypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseError handleTypeMismatchException(TypeMismatchException e) {
    return new BaseError(e.getMessage());
  }

  /**
   * Метод для перехвата HttpMessageNotReadableException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseError handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    return new BaseError(e.getMessage());
  }

  /**
   * Метод для перехвата ValidationExceptionList.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(ValidationExceptionList.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, List<String>> handleValidationExceptionList(ValidationExceptionList e) {
    return e.getErrorMessages();
  }

  /**
   * Метод для перехвата BadCredentialsException.
   *
   * @param e перехваченное исключение.
   * @return преобразованное сообщение для пользователя.
   */
  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public BaseError handleBadCredentialsException(BadCredentialsException e) {
    return new BaseError(ExceptionMessage.WRONG_LOGIN_OR_PASSWORD);
  }
}
