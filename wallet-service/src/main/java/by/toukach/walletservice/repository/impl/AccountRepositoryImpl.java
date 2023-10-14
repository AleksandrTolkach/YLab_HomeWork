package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.repository.AccountRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для выполнения запросов, связанных со счетом, в память.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountRepositoryImpl implements AccountRepository {

  private static final AccountRepository instance = new AccountRepositoryImpl();

  private final List<Account> accountList = new ArrayList<>();
  private Long sequence = 0L;

  @Override
  public Account createAccount(Account account) {
    account.setId(++sequence);
    accountList.add(account);
    return account;
  }

  @Override
  public Account findAccountById(Long id) {
    return accountList.stream()
        .filter(a -> a.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public static AccountRepository getInstance() {
    return instance;
  }
}
