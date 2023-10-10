package by.toukach.walletservice.domain.services.handler.impl;

import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.domain.services.AccountService;
import by.toukach.walletservice.domain.services.LoggerService;
import by.toukach.walletservice.domain.services.TransactionService;
import by.toukach.walletservice.domain.services.handler.TransactionHandler;
import by.toukach.walletservice.domain.services.impl.AccountServiceImpl;
import by.toukach.walletservice.domain.services.impl.LoggerServiceImpl;
import by.toukach.walletservice.domain.services.impl.TransactionServiceImpl;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exceptions.ArgumentValueException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import by.toukach.walletservice.infrastructure.entity.Log;
import by.toukach.walletservice.utils.LogUtil;

/**
 * Класс представляющий обработчик транзакций по начислению средств.
 * */
public class CreditTransactionHandler implements TransactionHandler {

  private final TransactionService transactionService;
  private final AccountService accountService;
  private final LoggerService loggerService;

  public CreditTransactionHandler() {
    transactionService = TransactionServiceImpl.getInstance();
    accountService = AccountServiceImpl.getInstance();
    loggerService = LoggerServiceImpl.getInstance();
  }

  @Override
  public TransactionDto handle(TransactionDto transactionDto) {
    AccountDto account = accountService.findAccountById(transactionDto.getAccountId());
    Double value = transactionDto.getValue();

    if (value <= 0) {
      throw new ArgumentValueException(ExceptionMessage.POSITIVE_ARGUMENT);
    }

    TransactionDto transaction = transactionService.createTransaction(transactionDto);

    Double sum = account.getSum();
    sum += value;
    account.setSum(sum);

    accountService.updateAccount(account);

    Log log = LogUtil.prepareLog(LogType.TRANSACTION,
        String.format(LogUtil.CREDIT, transactionDto.getUserId(), transactionDto.getAccountId(),
            transactionDto.getValue()));
    loggerService.createLog(log);

    return transaction;
  }

  @Override
  public TransactionType type() {
    return TransactionType.CREDIT;
  }
}
