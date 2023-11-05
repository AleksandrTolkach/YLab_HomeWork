package by.toukach.walletservice.service.transaction;

import by.toukach.walletservice.dto.CreateTransactionDto;
import by.toukach.walletservice.dto.TransactionDto;
import java.util.List;

/**
 * Интерфейс для выполнения операций с Transaction.
 * */
public interface TransactionService {

  /**
   * Метод для создания операции.
   *
   * @param createTransactionDto создаваемая операция.
   * @return созданная операция.
   */
  TransactionDto createTransaction(CreateTransactionDto createTransactionDto);

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
