package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.aspect.annotation.Loggable;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.mapper.AccountMapper;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.AccountRepository;
import by.toukach.walletservice.service.AccountService;
import by.toukach.walletservice.service.UserService;
import by.toukach.walletservice.validator.Validator;
import by.toukach.walletservice.validator.impl.ParamValidator.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Класс для выполнения операций со счетом.
 * */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private static final String ID = "id";
  private static final String USER_ID = "userId";

  private final AccountRepository accountRepository;
  private final UserService userService;
  private final Validator<AccountDto> accountDtoValidator;
  private final AccountMapper accountMapper;
  private final Validator<String> paramValidator;

  @Override
  @Loggable(type = LogType.CREATE_ACCOUNT)
  public AccountDto createAccount(AccountDto accountDto) {
    accountDtoValidator.validate(accountDto);
    UserDto userDto = userService.findUserById(accountDto.getUserId());

    if (userDto == null) {
      throw new EntityNotFoundException(ExceptionMessage.USER_BY_ID_NOT_FOUND);
    } else {
      accountDto.setCreatedAt(LocalDateTime.now());
      accountDto.setSum(BigDecimal.ZERO);

      Account account =
          accountRepository.createAccount(accountMapper.accountDtoToAccount(accountDto));
      accountDto = accountMapper.accountToAccountDto(account);

      return accountDto;
    }
  }

  @Override
  public AccountDto findAccountById(Long id) {
    paramValidator.validate(String.valueOf(id), Type.ID.name(), ID);
    Account account = accountRepository.findAccountById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format(ExceptionMessage.ACCOUNT_BY_ID_NOT_FOUND, id)));

    return accountMapper.accountToAccountDto(account);
  }

  @Override
  public List<AccountDto> findAccountsByUserId(Long userId) {
    paramValidator.validate(String.valueOf(userId), Type.ID.name(), USER_ID);
    return accountRepository.findAccountsByUserId(userId)
        .stream()
        .map(accountMapper::accountToAccountDto)
        .collect(Collectors.toList());
  }

  @Override
  public AccountDto updateAccount(AccountDto accountDto) {
    accountDtoValidator.validate(accountDto);
    Long userId = accountDto.getUserId();

    if (!userService.isExists(userId)) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, userId));
    }

    Long id = accountDto.getId();
    Account account = accountRepository.findAccountById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format(ExceptionMessage.ACCOUNT_BY_ID_NOT_FOUND, id)));

    account.setSum(accountDto.getSum());
    account.setTitle(accountDto.getTitle());

    account = accountRepository.updateAccount(account).orElseThrow(() ->
        new EntityNotFoundException(String.format(ExceptionMessage.ACCOUNT_BY_ID_NOT_FOUND, id)));

    return accountMapper.accountToAccountDto(account);
  }
}
