package by.toukach.walletservice.service.handler.impl;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exceptions.ArgumentValueException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import by.toukach.walletservice.exceptions.InsufficientFundsException;
import by.toukach.walletservice.service.AccountService;
import by.toukach.walletservice.service.LoggerService;
import by.toukach.walletservice.service.TransactionService;
import by.toukach.walletservice.service.handler.TransactionHandler;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.TransactionServiceImpl;
import by.toukach.walletservice.utils.LogUtil;

/**
 * Класс представляющий обработчик транзакций по списанию средств.
 * */
public class DebitTransactionHandler implements TransactionHandler {

  private final TransactionService transactionService;
  private final AccountService accountService;
  private final LoggerService loggerService;

  public DebitTransactionHandler() {
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

    Double sum = account.getSum();

    if (sum < value) {
      throw new InsufficientFundsException(ExceptionMessage.INSUFFICIENT_FUNDS);
    }

    transactionDto = transactionService.createTransaction(transactionDto);

    sum -= value;
    account.setSum(sum);

    accountService.updateAccount(account);

    Log log = LogUtil.prepareLog(LogType.TRANSACTION,
        String.format(LogUtil.DEBIT, transactionDto.getUserId(), transactionDto.getAccountId(),
            transactionDto.getValue()));
    loggerService.createLog(log);

    return transactionDto;
  }

  @Override
  public TransactionType type() {
    return TransactionType.DEBIT;
  }
}
