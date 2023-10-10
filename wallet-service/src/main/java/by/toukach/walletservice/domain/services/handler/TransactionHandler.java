package by.toukach.walletservice.domain.services.handler;

import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.enumiration.TransactionType;

/**
 * Интерфейс представляющий обработчик транзакций.
 * */
public interface TransactionHandler {

  /**
   * Метод для обработки транзакции.
   *
   * @param transaction транзакция для обработки.
   * @return обработанная транзакция.
   */
  TransactionDto handle(TransactionDto transaction);

  /**
   * Метод для получения типа обработчика.
   *
   * @return тип обработчика.
   */
  TransactionType type();
}
