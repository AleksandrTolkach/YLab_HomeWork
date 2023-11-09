package by.toukach.logger.service.impl;

import by.toukach.logger.enumiration.LogType;
import by.toukach.logger.service.LogProvider;
import by.toukach.logger.service.LogProviderFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс представляет фабрику, ответственную за создание LopProvider.
 */
public class LogProviderFactoryImpl implements LogProviderFactory {

  private Map<LogType, LogProvider> logProviderMap;

  /**
   * Конструктор для создания LogProviderFactoryImpl.
   *
   * @param logProviderList список всех реализаций LogProvider.
   */
  public LogProviderFactoryImpl(List<LogProvider> logProviderList) {
    logProviderMap = logProviderList.stream()
        .collect(Collectors.toMap(LogProvider::type, provider -> provider));
  }

  @Override
  public LogProvider getProvider(LogType logType) {
    return logProviderMap.get(logType);
  }
}
