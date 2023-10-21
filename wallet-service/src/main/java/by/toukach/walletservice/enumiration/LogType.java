package by.toukach.walletservice.enumiration;

/**
 * Перечисление возможных логов в банке.
 * */
public enum LogType {

  TRANSACTION("Транзакция"),
  AUTH("Авторизация"),
  ACCOUNT("Операция над счетом");
  
  private final String value;

  LogType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
