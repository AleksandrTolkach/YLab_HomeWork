package by.toukach.walletservice.service;

import by.toukach.walletservice.dto.AccountDto;

/**
 * Интерфейс для выполнения операций со счетом.
 * */
public interface AccountService {

  /**
   * Метод для создания счета.
   *
   * @param accountDto создаваемый счет.
   * @param userId id владельца счета.
   * @return созданный счет.
   */
  AccountDto createAccount(AccountDto accountDto, Long userId);

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
   * @param accountDto обновляемый счет.
   * @return обновленный счет.
   */
  AccountDto updateAccount(AccountDto accountDto);
}
