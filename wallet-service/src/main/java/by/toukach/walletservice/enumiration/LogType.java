package by.toukach.walletservice.enumiration;

/**
 * Перечисление возможных логов в банке.
 * */
public enum LogType {

  CREDIT("Пополнение", "Пользователь %s пополнил счет %s. Сумма %s."),
  DEBIT("Снятие", "Пользователь %s списал средства со счета %s. Сумма %s."),
  LOG_IN("Вход", "Пользователь %s вошел."),
  SIGN_UP("Регистрация", "Пользователь %s зарегистрировался."),
  LOG_OUT("Выход", "Пользователь %s завершил работу."),
  CREATE_ACCOUNT("Создание счета", "Пользователь %s создал счет %s.");
  
  private final String value;
  private final String message;

  LogType(String value, String message) {
    this.value = value;
    this.message = message;
  }

  public String getValue() {
    return value;
  }

  public String getMessage() {
    return message;
  }
}
