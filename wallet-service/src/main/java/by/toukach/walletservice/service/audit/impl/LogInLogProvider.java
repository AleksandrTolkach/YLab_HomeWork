package by.toukach.walletservice.service.audit.impl;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.enumiration.LogType;
import by.toukach.logger.service.LogProvider;
import by.toukach.walletservice.dto.LogInResponseDto;
import org.springframework.stereotype.Component;

/**
 * Класс для обработки и подготовки логов входа в приложение.
 */
@Component
public class LogInLogProvider implements LogProvider {

  @Override
  public LogDto provideLog(Object object) {
    LogInResponseDto logInResponseDto = (LogInResponseDto) object;
    String message = String.format(LogType.LOG_IN.getMessage(),
        logInResponseDto.getUserDto().getId());
    return LogDto.builder()
        .type(LogType.LOG_IN)
        .message(message)
        .build();
  }

  @Override
  public LogType type() {
    return LogType.LOG_IN;
  }
}
