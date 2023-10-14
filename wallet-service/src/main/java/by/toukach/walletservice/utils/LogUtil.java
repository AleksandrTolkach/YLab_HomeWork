package by.toukach.walletservice.utils;

import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.enumiration.LogType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * Утилитарный класс для работы с логами.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogUtil {

  public static final String LOGIN = "Пользователь с ID %s вошел в приложение.";
  public static final String SIGN_UP = "Пользователь зарегистрировался. ID = %s";
  public static final String LOGOUT = "Пользователь с ID %s завершил работу.";
  public static final String CREATE_ACCOUNT = "Пользователь с ID %s создал счет с ID %s.";
  public static final String CREDIT =
      "Пользователь c ID %s пополнил счет с ID %s. Сумма = %s";
  public static final String DEBIT =
      "Пользователь с ID %s снял средства со счета с ID %s. Сумма = %s";

  /**
   * Метод создает лог по предоставленным аргументам.
   *
   * @param type тип лога.
   * @param message сообщение лога.
   * @return подготовленный лог.
   */
  public static Log prepareLog(LogType type, String message) {
    return Log.builder()
        .type(type)
        .value(message)
        .createdAt(LocalDateTime.now())
        .build();
  }
}
