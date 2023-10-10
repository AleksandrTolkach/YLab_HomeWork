package by.toukach.walletservice.infrastructure.repositories;

import by.toukach.walletservice.infrastructure.entity.TransactionEntity;
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
  TransactionEntity createTransaction(TransactionEntity transaction);

  /**
   * Метод для чтения операции из памяти.
   *
   * @param id id запрашиваемой операции.
   * @return запрашиваемая операция.
   */
  TransactionEntity findTransactionById(Long id);

  /**
   * Метод для чтения операций из памяти по id пользователя.
   *
   * @param userId id пользователя.
   * @return запрашиваемые операции.
   */
  List<TransactionEntity> findTransactionByUserId(Long userId);

  /**
   * Метод для проверки существования операции в памяти по id.
   *
   * @param id id операции.
   * @return true, если операция с указанным id существует, false если нет.
   */
  boolean isExists(Long id);
}
