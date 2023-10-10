package by.toukach.walletservice.domain.services;

import by.toukach.walletservice.domain.models.TransactionDto;
import java.util.List;

/**
 * Интерфейс для выполнения операций с Transaction.
 * */
public interface TransactionService {

  /**
   * Метод для создания операции.
   *
   * @param transaction создаваемая операция.
   * @return созданная операция.
   */
  TransactionDto createTransaction(TransactionDto transaction);

  /**
   * Метод для чтения операции.
   *
   * @param id id запрашиваемой операции.
   * @return запрашиваемая операция.
   */
  TransactionDto findTransactionById(Long id);

  /**
   * Метод для чтения операции по id пользователя.
   *
   * @param userId id пользователя.
   * @return запрашиваемые операции.
   */
  List<TransactionDto> findTransactionByUserId(Long userId);
}
