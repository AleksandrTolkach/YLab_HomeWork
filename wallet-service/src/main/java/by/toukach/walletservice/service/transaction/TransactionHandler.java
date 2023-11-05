package by.toukach.walletservice.service.transaction;

import by.toukach.walletservice.dto.CreateTransactionDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.enumiration.TransactionType;

/**
 * Интерфейс представляющий обработчик транзакций.
 * */
public interface TransactionHandler {

  /**
   * Метод для обработки транзакции.
   *
   * @param createTransactionDto транзакция для обработки.
   * @return обработанная транзакция.
   */
  TransactionDto handle(CreateTransactionDto createTransactionDto);

  /**
   * Метод для получения типа обработчика.
   *
   * @return тип обработчика.
   */
  TransactionType type();
}
