package by.toukach.walletservice.exception;

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
  public static final String TRANSACTION_NOT_FOUND = "Транзакции с id %s не существует.";
  public static final String POSITIVE_ARGUMENT = "Значение должно быть положительным.";
  public static final String INSUFFICIENT_FUNDS = "Недостаточно средств.";
  public static final String WRONG_LOGIN_OR_PASSWORD = "Неверный логин или пароль.";
  public static final String WRONG_SYMBOL_MESSAGE = "Использован неверный символ.\n";
  public static final String DB_DRIVER_DID_NOT_LOAD = "Не удалось загрузить драйвер базы данных.\n";
  public static final String CONNECT_TO_DB = "Не удалось подключиться к базе данных.\n";
  public static final String CLOSE_CONNECTION_TO_DB =
      "Не удалось закрыть соединение к базе данных.\n";
  public static final String MIGRATION = "В процессе миграции базы данных произошла ошибка.\n";
  public static final String USER_NOT_FOUND = "Пользователь с %s %s не найден\n";
  public static final String DB_REQUEST = "Не удалось выполнить запрос в базу\n";
  public static final String SAVE_ACCOUNT = "Не удалось сохранить счет в базу данных.\n";
  public static final String SAVE_TRANSACTION = "Не удалось сохранить транзакцию в базу\n";
  public static final String ACCOUNT_NOT_FOUND = "Счет с id %s не найден\n";
  public static final String ACCOUNT_UPDATE = "Не удалось обновить счет в базе\n";
  public static final String SAVE_LOG = "Не удалось сохранить лок в базу данных.\n";
  public static final String SAVE_USER = "Не удалось сохранить пользователя в базу данных.\n";

}
