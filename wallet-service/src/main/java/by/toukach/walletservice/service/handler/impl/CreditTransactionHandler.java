package by.toukach.walletservice.service.handler.impl;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exceptions.ArgumentValueException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import by.toukach.walletservice.service.AccountService;
import by.toukach.walletservice.service.LoggerService;
import by.toukach.walletservice.service.TransactionService;
import by.toukach.walletservice.service.handler.TransactionHandler;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.TransactionServiceImpl;
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
    AccountDto accountDto = accountService.findAccountById(transactionDto.getAccountId());
    Double value = transactionDto.getValue();

    if (value <= 0) {
      throw new ArgumentValueException(ExceptionMessage.POSITIVE_ARGUMENT);
    }

    transactionDto = transactionService.createTransaction(transactionDto);

    Double sum = accountDto.getSum();
    sum += value;
    accountDto.setSum(sum);

    accountService.updateAccount(accountDto);

    Log log = LogUtil.prepareLog(LogType.TRANSACTION,
        String.format(LogUtil.CREDIT, transactionDto.getUserId(), transactionDto.getAccountId(),
            transactionDto.getValue()));
    loggerService.createLog(log);

    return transactionDto;
  }

  @Override
  public TransactionType type() {
    return TransactionType.CREDIT;
  }
}
