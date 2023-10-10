package by.toukach.walletservice.infrastructure.repositories.impl;

import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.repositories.AccountRepository;
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

  private final List<AccountEntity> accountList = new ArrayList<>();
  private Long sequence = 0L;

  @Override
  public AccountEntity createAccount(AccountEntity account) {
    account.setId(++sequence);
    accountList.add(account);
    return account;
  }

  @Override
  public AccountEntity findAccountById(Long id) {
    return accountList.stream()
        .filter(a -> a.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public static AccountRepository getInstance() {
    return instance;
  }
}
