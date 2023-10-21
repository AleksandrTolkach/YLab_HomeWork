package by.toukach.walletservice.repository;

import by.toukach.walletservice.entity.Account;
import java.util.List;

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

  /**
   * Метод для чтения счетов из памяти по ID пользователя.
   *
   * @param userId id счета.
   * @return запрашиваемый счет.
   */
  List<Account> findAccountsByUserId(Long userId);

  /**
   * Метод для обновления счета в базе.
   *
   * @param account обновляемый счет.
   * @return обновленный счет.
   */
  Account updateAccount(Account account);
}
