package by.toukach.walletservice.service.audit.impl;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.enumiration.LogType;
import by.toukach.logger.service.LogProvider;
import by.toukach.walletservice.dto.LogInResponseDto;
import org.springframework.stereotype.Component;

/**
 * Класс для обработки и подготовки логов выхода из приложения.
 */
@Component
public class LogOutLogProvider implements LogProvider {

  @Override
  public LogDto provideLog(Object object) {
    LogInResponseDto logInResponseDto = (LogInResponseDto) object;
    String message = String.format(LogType.LOG_OUT.getMessage(),
        logInResponseDto.getUserDto().getId());
    return LogDto.builder()
        .type(LogType.LOG_OUT)
        .message(message)
        .build();
  }

  @Override
  public LogType type() {
    return LogType.LOG_OUT;
  }
}
