package by.toukach.logger.repository;

import by.toukach.logger.entity.Log;
import java.util.List;

/**
 * Интерфейс для выполнения запросов, связанных с логами, в память.
 * */
public interface LoggerRepository {

  /**
   * Метод для записи лога в память.
   *
   * @param log записываемый лог.
   * @return записанный лог.
   */
  Log createLog(Log log);

  /**
   * Метод для чтения логов из памяти.
   *
   * @return список логов.
   */
  List<Log> findLogs();
}
