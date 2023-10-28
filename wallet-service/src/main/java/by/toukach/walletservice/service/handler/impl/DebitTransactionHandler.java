package by.toukach.walletservice.service.handler.impl;

import by.toukach.walletservice.aspect.annotation.Loggable;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.ArgumentValueException;
import by.toukach.walletservice.exception.EntityConflictException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.InsufficientFundsException;
import by.toukach.walletservice.security.SecurityContext;
import by.toukach.walletservice.service.AccountService;
import by.toukach.walletservice.service.TransactionService;
import by.toukach.walletservice.service.handler.TransactionHandler;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.TransactionServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Класс представляющий обработчик транзакций по списанию средств.
 * */
@Service
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DebitTransactionHandler implements TransactionHandler {

  private final TransactionService transactionService;
  private final AccountService accountService;

  @Override
  @Loggable(type = LogType.DEBIT)
  public TransactionDto handle(TransactionDto transactionDto) {
    AccountDto accountDto = accountService.findAccountById(transactionDto.getAccountId());

    UserDto currentUser = SecurityContext.getCurrentUser();
    Optional<AccountDto> optionalAccountDtoOfCurrentUser = currentUser.getAccountList().stream()
        .filter(a -> a.getId().equals(accountDto.getId()))
        .findFirst();

    if (!currentUser.getId().equals(transactionDto.getUserId())) {
      throw new EntityConflictException(ExceptionMessage.ELSE_USER);
    } else if (optionalAccountDtoOfCurrentUser.isEmpty()) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.ACCOUNT_NOT_FOUND, transactionDto.getAccountId()));
    }

    BigDecimal value = transactionDto.getValue();

    if (value.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ArgumentValueException(ExceptionMessage.POSITIVE_ARGUMENT);
    }

    BigDecimal sum = accountDto.getSum();

    if (sum.compareTo(value) < 0) {
      throw new InsufficientFundsException(ExceptionMessage.INSUFFICIENT_FUNDS);
    }

    transactionDto = transactionService.createTransaction(transactionDto);

    sum = sum.subtract(value);
    accountDto.setSum(sum);

    accountService.updateAccount(accountDto);

    return transactionDto;
  }

  @Override
  public TransactionType type() {
    return TransactionType.DEBIT;
  }
}
