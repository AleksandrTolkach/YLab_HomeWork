package by.toukach.walletservice.service.audit.impl;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.enumiration.LogType;
import by.toukach.logger.service.LogProvider;
import by.toukach.walletservice.dto.AccountDto;
import org.springframework.stereotype.Component;

/**
 * Класс для обработки и подготовки логов по созданию счета.
 */
@Component
public class AccountCreatedLogProvider implements LogProvider {

  @Override
  public LogDto provideLog(Object object) {
    AccountDto accountDto = (AccountDto) object;
    String message = String.format(LogType.CREATE_ACCOUNT.getMessage(), accountDto.getUserId(),
        accountDto.getId());
    return LogDto.builder()
        .type(LogType.CREATE_ACCOUNT)
        .message(message)
        .build();
  }

  @Override
  public LogType type() {
    return LogType.CREATE_ACCOUNT;
  }
}
