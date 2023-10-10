package by.toukach.walletservice.domain.services;

import by.toukach.walletservice.domain.models.AccountDto;

/**
 * Интерфейс для выполнения операций со счетом.
 * */
public interface AccountService {

  /**
   * Метод для создания счета.
   *
   * @param account создаваемый счет.
   * @param userId id владельца счета.
   * @return созданный счет.
   */
  AccountDto createAccount(AccountDto account, Long userId);

  /**
   * Метод для чтения счета из памяти.
   *
   * @param id id запрашиваемого счета.
   * @return запрашиваемый счет.
   */
  AccountDto findAccountById(Long id);

  /**
   * Метод для обновления счета в памяти.
   *
   * @param account обновляемый счет.
   * @return обновленный счет.
   */
  AccountDto updateAccount(AccountDto account);
}
