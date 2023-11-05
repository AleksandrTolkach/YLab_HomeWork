package by.toukach.walletservice.service.account;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.CreateAccountDto;
import java.util.List;

/**
 * Интерфейс для выполнения операций со счетом.
 * */
public interface AccountService {

  /**
   * Метод для создания счета.
   *
   * @param createAccountDto создаваемый счет.
   * @return созданный счет.
   */
  AccountDto createAccount(CreateAccountDto createAccountDto);

  /**
   * Метод для чтения счета из памяти.
   *
   * @param id id запрашиваемого счета.
   * @return запрашиваемый счет.
   */
  AccountDto findAccountById(Long id);

  /**
   * Метод для чтения счетов из памяти по ID пользователя.
   *
   * @param userId id пользователя.
   * @return запрашиваемые счета.
   */
  List<AccountDto> findAccountsByUserId(Long userId);

  /**
   * Метод для обновления счета в памяти.
   *
   * @param accountDto обновляемый счет.
   * @return обновленный счет.
   */
  AccountDto updateAccount(AccountDto accountDto);
}
