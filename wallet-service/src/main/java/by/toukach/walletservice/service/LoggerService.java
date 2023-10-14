package by.toukach.walletservice.service;

import by.toukach.walletservice.entity.Log;
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
  Log createLog(Log log);

  /**
   * Метод для чтения логов из памяти.
   *
   * @return запрашиваемые логи.
   */
  List<Log> findLogs();
}
