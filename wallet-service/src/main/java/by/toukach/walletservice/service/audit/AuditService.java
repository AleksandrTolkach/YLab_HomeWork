package by.toukach.walletservice.service.audit;

import by.toukach.logger.dto.LogDto;
import java.util.List;

/**
 * Интерфейс для выполнения операций с аудитом.
 */
public interface AuditService {

  /**
   * Метод для получения списка всех логов.
   *
   * @return запрашиваемые логи.
   */
  List<LogDto> findLogs();
}
