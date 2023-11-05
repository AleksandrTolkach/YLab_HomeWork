package by.toukach.logger.service;

import by.toukach.logger.enumiration.LogType;

/**
 * Интерфейс, представляющий фабрику, для создания LogProvider.
 */
public interface LogProviderFactory {

  /**
   * Метод передает LogProvider.
   *
   * @param logType тип лога.
   * @return запрашиваемый LogProvider.
   */
  LogProvider getProvider(LogType logType);
}
