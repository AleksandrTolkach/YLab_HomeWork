package by.toukach.walletservice.repository;

import by.toukach.walletservice.entity.Transaction;
import java.util.List;

/**
 * Интерфейс для выполнения запросов, связанных с операциями, в память.
 * */
public interface TransactionRepository {

  /**
   * Метод для создания операции в памяти.
   *
   * @param transaction операция для создания.
   * @return созданная транзакция.
   */
  Transaction createTransaction(Transaction transaction);

  /**
   * Метод для чтения операции из памяти.
   *
   * @param id id запрашиваемой операции.
   * @return запрашиваемая операция.
   */
  Transaction findTransactionById(Long id);

  /**
   * Метод для чтения операций из памяти по id пользователя.
   *
   * @param userId id пользователя.
   * @return запрашиваемые операции.
   */
  List<Transaction> findTransactionByUserId(Long userId);

  /**
   * Метод для проверки существования операции в памяти по id.
   *
   * @param id id операции.
   * @return true, если операция с указанным id существует, false если нет.
   */
  boolean isExists(Long id);
}
