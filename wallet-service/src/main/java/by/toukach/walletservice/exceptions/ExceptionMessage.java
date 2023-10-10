package by.toukach.walletservice.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс, содержащий сообщения об ошибках.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {

  public static final String USER_BY_ID_NOT_FOUND = "Пользователь с id %s не найден.";
  public static final String USER_BY_LOGIN_NOT_FOUND = "Пользователь с login %s не найден.";
  public static final String USER_DUPLICATE = "Пользователь с логином %s уже существует.";
  public static final String ACCOUNT_BY_ID_NOT_FOUND = "Счет с id %s не существует.";
  public static final String TRANSACTION_DUPLICATE = "Транзакция с id %s уже существует.";
  public static final String TRANSACTION_NOT_FOUND = "Транзакции с id %s не существует.";
  public static final String POSITIVE_ARGUMENT = "Значение должно быть положительным.";
  public static final String INSUFFICIENT_FUNDS = "Недостаточно средств.";
  public static final String WRONG_LOGIN_OR_PASSWORD = "Неверный логин или пароль.";
  public static final String WRONG_SYMBOL_MESSAGE = "Использован неверный символ.\n";
  public static final String LOG_NOT_FOUND = "log с id %s не найден.";
}
