package by.toukach.walletservice.service.audit.impl;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.enumiration.LogType;
import by.toukach.logger.service.LogProvider;
import by.toukach.walletservice.dto.LogInResponseDto;
import org.springframework.stereotype.Component;

/**
 * Класс для обработки и подготовки логов регестрирования в приложение.
 */
@Component
public class SignUpLogProvider implements LogProvider {

  @Override
  public LogDto provideLog(Object object) {
    LogInResponseDto logInResponseDto = (LogInResponseDto) object;
    String message = String.format(LogType.SIGN_UP.getMessage(),
        logInResponseDto.getUserDto().getId());
    return LogDto.builder()
        .type(LogType.SIGN_UP)
        .message(message)
        .build();
  }

  @Override
  public LogType type() {
    return LogType.SIGN_UP;
  }
}
