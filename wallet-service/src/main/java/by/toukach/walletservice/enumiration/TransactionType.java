package by.toukach.walletservice.enumiration;

/**
 * Перечисление возможных операций в банке.
 * */
public enum TransactionType {

  DEBIT("Снятие"),
  CREDIT("Пополнение");

  private final String value;

  TransactionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
