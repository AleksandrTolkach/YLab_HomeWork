package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.converter.Converter;
import by.toukach.walletservice.entity.converter.impl.AccountConverter;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.AccountRepository;
import by.toukach.walletservice.repository.impl.AccountRepositoryImpl;
import by.toukach.walletservice.service.AccountService;
import by.toukach.walletservice.service.LoggerService;
import by.toukach.walletservice.service.UserService;
import by.toukach.walletservice.utils.LogUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для выполнения операций со счетом.
 * */
public class AccountServiceImpl implements AccountService {

  private static final AccountService instance = new AccountServiceImpl();

  private final AccountRepository accountRepository;
  private final Converter<Account, AccountDto> accountConverter;
  private final UserService userService;
  private final LoggerService loggerService;

  private AccountServiceImpl() {
    accountRepository = AccountRepositoryImpl.getInstance();
    accountConverter = AccountConverter.getInstance();
    userService = UserServiceImpl.getInstance();
    loggerService = LoggerServiceImpl.getInstance();
  }

  @Override
  public AccountDto createAccount(AccountDto accountDto) {
    UserDto userDto = userService.findUserById(accountDto.getUserId());

    if (userDto == null) {
      throw new EntityNotFoundException(ExceptionMessage.USER_BY_ID_NOT_FOUND);
    } else {
      accountDto.setCreatedAt(LocalDateTime.now());

      Account account = accountRepository.createAccount(accountConverter.toEntity(accountDto));
      accountDto = accountConverter.toDto(account);

      Log log = LogUtil.prepareLog(LogType.ACCOUNT, String.format(LogUtil.CREATE_ACCOUNT,
          userDto.getId(), accountDto.getId()));

      loggerService.createLog(log);
      return accountDto;
    }
  }

  @Override
  public AccountDto findAccountById(Long id) {
    Account account = accountRepository.findAccountById(id);

    if (account == null) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.ACCOUNT_BY_ID_NOT_FOUND, id));
    }

    return accountConverter.toDto(account);
  }

  @Override
  public List<AccountDto> findAccountsByUserId(Long userId) {
    return accountRepository.findAccountsByUserId(userId)
        .stream()
        .map(accountConverter::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public AccountDto updateAccount(AccountDto accountDto) {
    Long userId = accountDto.getUserId();

    if (!userService.isExists(userId)) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, userId));
    }

    Long id = accountDto.getId();
    Account account = accountRepository.findAccountById(id);

    if (account == null) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.ACCOUNT_BY_ID_NOT_FOUND, id));
    }

    account.setSum(accountDto.getSum());
    account.setTitle(accountDto.getTitle());

    account = accountRepository.updateAccount(account);

    return accountConverter.toDto(account);
  }

  public static AccountService getInstance() {
    return instance;
  }
}
