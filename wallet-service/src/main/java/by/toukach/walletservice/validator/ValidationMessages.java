package by.toukach.walletservice.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс содержащий сообщения об ошибках валидации.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationMessages {

  public static final String FIELD_NULL = "Не передано значение в поле";
  public static final String ENTITY_NULL = "Не передана сущность %s";
  public static final String NEGATIVE_ARGUMENT = "Значение должно быть больше нуля";
  public static final String UNKNOWN_TRANSACTION = "Указанный тип транзакции не существует";
  public static final String WRONG_TYPE = "Передан неверный тип данных";
}
