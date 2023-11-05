package by.toukach.walletservice.validator;

/**
 * Интерфейс для валидации объектов.
 *
 * @param <I> объект для валидации.
 */
public interface Validator<I> {

  /**
   * Метод для выполнения валидации объекта.
   *
   * @param item объект валидации.
   * @param params параметры для валидации.
   */
  void validate(I item, String... params);
}
