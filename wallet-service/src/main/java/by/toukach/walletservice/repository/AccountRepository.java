package by.toukach.walletservice.repository;

import by.toukach.walletservice.entity.Account;

/**
 * Интерфейс для выполнения запросов, связанных со счетом, в память.
 * */
public interface AccountRepository {

  /** Метод для создания счета в памяти.
   *
   * @param account создаваемый счет.
   * @return созданный счет.
   */
  Account createAccount(Account account);

  /**
   * Метод для чтения счета из памяти.
   *
   * @param id id счета.
   * @return запрашиваемый счет.
   */
  Account findAccountById(Long id);
}
