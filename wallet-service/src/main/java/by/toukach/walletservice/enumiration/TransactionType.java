package by.toukach.walletservice.enumiration;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Перечисление возможных операций в банке.
 * */
public enum TransactionType {

  DEBIT("Снятие"),
  CREDIT("Пополнение"),
  @JsonEnumDefaultValue
  UNKNOWN("Неизвестная");

  private final String value;

  TransactionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
