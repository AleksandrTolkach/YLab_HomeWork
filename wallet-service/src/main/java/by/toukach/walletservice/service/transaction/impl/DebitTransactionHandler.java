package by.toukach.walletservice.service.transaction.impl;

import by.toukach.logger.aspect.annotation.Loggable;
import by.toukach.logger.enumiration.LogType;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.CreateTransactionDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.ArgumentValueException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.InsufficientFundsException;
import by.toukach.walletservice.service.account.AccountService;
import by.toukach.walletservice.service.transaction.TransactionHandler;
import by.toukach.walletservice.service.transaction.TransactionService;
import by.toukach.walletservice.utils.SecurityUtil;
import by.toukach.walletservice.validator.Validator;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Класс представляющий обработчик транзакций по списанию средств.
 * */
@Service
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DebitTransactionHandler implements TransactionHandler {

  private final TransactionService transactionService;
  private final AccountService accountService;
  private final Validator<TransactionDto> transactionDtoValidator;
  private final Validator<CreateTransactionDto> createTransactionDtoValidator;

  @Override
  @Loggable(type = LogType.DEBIT)
  @Transactional
  public TransactionDto handle(CreateTransactionDto createTransactionDto) {
    createTransactionDtoValidator.validate(createTransactionDto);

    AccountDto accountDto = accountService.findAccountById(createTransactionDto.getAccountId());

    if (!accountDto.getUserId().equals(SecurityUtil.getUserDetails().getId())) {
      throw new ArgumentValueException(ExceptionMessage.SOMEONE_ELSE_ACCOUNT);
    }

    BigDecimal value = createTransactionDto.getValue();

    if (value.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ArgumentValueException(ExceptionMessage.POSITIVE_ARGUMENT);
    }

    BigDecimal sum = accountDto.getSum();

    if (sum.compareTo(value) < 0) {
      throw new InsufficientFundsException(ExceptionMessage.INSUFFICIENT_FUNDS);
    }

    sum = sum.subtract(value);
    accountDto.setSum(sum);

    accountService.updateAccount(accountDto);

    return transactionService.createTransaction(createTransactionDto);
  }

  @Override
  public TransactionType type() {
    return TransactionType.DEBIT;
  }
}
