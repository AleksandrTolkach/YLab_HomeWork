package by.toukach.walletservice.service;

import by.toukach.walletservice.dto.LogDto;
import java.util.List;

/**
 * Интерфейс для работы с пользовательскими логами.
 */
public interface LoggerService {

  /**
   * Метод для записи лога.
   *
   * @param log записываемый лог.
   * @return записанный лог.
   */
  LogDto createLog(LogDto log);

  /**
   * Метод для чтения логов из памяти.
   *
   * @return запрашиваемые логи.
   */
  List<LogDto> findLogs();
}
