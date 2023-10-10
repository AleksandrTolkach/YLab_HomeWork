package by.toukach.walletservice.infrastructure.repositories;

import by.toukach.walletservice.infrastructure.entity.AccountEntity;

/**
 * Интерфейс для выполнения запросов, связанных со счетом, в память.
 * */
public interface AccountRepository {

  /** Метод для создания счета в памяти.
   *
   * @param account создаваемый счет.
   * @return созданный счет.
   */
  AccountEntity createAccount(AccountEntity account);

  /**
   * Метод для чтения счета из памяти.
   *
   * @param id id счета.
   * @return запрашиваемый счет.
   */
  AccountEntity findAccountById(Long id);
}
