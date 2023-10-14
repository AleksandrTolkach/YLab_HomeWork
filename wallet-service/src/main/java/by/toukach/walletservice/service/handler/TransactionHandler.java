package by.toukach.walletservice.service.handler;

import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.enumiration.TransactionType;

/**
 * Интерфейс представляющий обработчик транзакций.
 * */
public interface TransactionHandler {

  /**
   * Метод для обработки транзакции.
   *
   * @param transactionDto транзакция для обработки.
   * @return обработанная транзакция.
   */
  TransactionDto handle(TransactionDto transactionDto);

  /**
   * Метод для получения типа обработчика.
   *
   * @return тип обработчика.
   */
  TransactionType type();
}
