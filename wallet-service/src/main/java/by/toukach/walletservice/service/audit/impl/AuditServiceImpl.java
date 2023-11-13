package by.toukach.walletservice.service.audit.impl;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.service.LoggerService;
import by.toukach.walletservice.service.audit.AuditService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Класс для выполнения операций с аудитом.
 */
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

  private final LoggerService loggerService;

  @Override
  public List<LogDto> findLogs() {
    return loggerService.findLogs();
  }
}
