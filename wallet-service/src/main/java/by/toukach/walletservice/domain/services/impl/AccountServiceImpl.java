package by.toukach.walletservice.domain.services.impl;

import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.AccountService;
import by.toukach.walletservice.domain.services.LoggerService;
import by.toukach.walletservice.domain.services.UserService;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.entity.Log;
import by.toukach.walletservice.infrastructure.entity.converter.Converter;
import by.toukach.walletservice.infrastructure.entity.converter.impl.AccountConverter;
import by.toukach.walletservice.infrastructure.repositories.AccountRepository;
import by.toukach.walletservice.infrastructure.repositories.impl.AccountRepositoryImpl;
import by.toukach.walletservice.utils.LogUtil;

/**
 * Класс для выполнения операций со счетом.
 * */
public class AccountServiceImpl implements AccountService {

  private static final AccountService instance = new AccountServiceImpl();

  private final AccountRepository accountRepository;
  private final Converter<AccountEntity, AccountDto> accountConverter;
  private final UserService userService;
  private final LoggerService loggerService;

  private AccountServiceImpl() {
    accountRepository = AccountRepositoryImpl.getInstance();
    accountConverter = AccountConverter.getInstance();
    userService = UserServiceImpl.getInstance();
    loggerService = LoggerServiceImpl.getInstance();
  }

  @Override
  public AccountDto createAccount(AccountDto account, Long userId) {
    UserDto user = userService.findUserById(userId);
    if (user == null) {
      throw new EntityNotFoundException(ExceptionMessage.USER_BY_ID_NOT_FOUND);
    }
    AccountEntity accountEntity =
        accountRepository.createAccount(accountConverter.toEntity(account));
    account = accountConverter.toDto(accountEntity);
    user.getAccountList().add(account);
    userService.updateUser(user);

    Log log = LogUtil.prepareLog(LogType.ACCOUNT, String.format(LogUtil.CREATE_ACCOUNT,
        user.getId(), account.getId()));

    loggerService.createLog(log);
    return account;
  }

  @Override
  public AccountDto findAccountById(Long id) {
    AccountEntity accountEntity = accountRepository.findAccountById(id);
    if (accountEntity == null) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.ACCOUNT_BY_ID_NOT_FOUND, id));
    }
    return accountConverter.toDto(accountEntity);
  }

  @Override
  public AccountDto updateAccount(AccountDto account) {
    Long id = account.getId();
    AccountEntity accountEntity = accountRepository.findAccountById(id);
    if (accountEntity == null) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.ACCOUNT_BY_ID_NOT_FOUND, id));
    }

    accountEntity.setSum(account.getSum());
    accountEntity.setTitle(account.getTitle());

    return accountConverter.toDto(accountEntity);
  }

  public static AccountService getInstance() {
    return instance;
  }
}
