package by.toukach.walletservice.service.audit.impl;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.enumiration.LogType;
import by.toukach.logger.service.LogProvider;
import by.toukach.walletservice.dto.TransactionDto;
import org.springframework.stereotype.Component;

/**
 * Класс для обработки и подготовки логов по созданию кредитной транзакции.
 */
@Component
public class CreditLogProvider implements LogProvider {

  @Override
  public LogDto provideLog(Object object) {
    TransactionDto transactionDto = (TransactionDto) object;
    String message = String.format(LogType.CREDIT.getMessage(), transactionDto.getUserId(),
        transactionDto.getAccountId(), transactionDto.getValue());
    return LogDto.builder()
        .type(LogType.CREDIT)
        .message(message)
        .build();
  }

  @Override
  public LogType type() {
    return LogType.CREDIT;
  }
}
