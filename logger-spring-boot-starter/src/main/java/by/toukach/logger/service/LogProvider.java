package by.toukach.logger.service;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.enumiration.LogType;

/**
 * Интерфейс для обработки и подготовки логов.
 */
public interface LogProvider {

  /**
   * Метод выполняет создание лога, на основе переданного объекта.
   *
   * @param object предмет логирования.
   * @return подготовленный лог.
   */
  LogDto provideLog(Object object);

  /**
   * Метод возвращает тип реализованного LogProvider.
   *
   * @return запрашиваемый LogType.
   */
  LogType type();
}
