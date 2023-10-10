package by.toukach.walletservice.domain.services.impl;

import by.toukach.walletservice.domain.services.LoggerService;
import by.toukach.walletservice.infrastructure.entity.Log;
import by.toukach.walletservice.infrastructure.repositories.LoggerRepository;
import by.toukach.walletservice.infrastructure.repositories.impl.LoggerRepositoryImpl;
import java.util.List;

/**
 * Класс для выполнения операций с логами.
 * */
public class LoggerServiceImpl implements LoggerService {

  private static final LoggerService instance = new LoggerServiceImpl();

  private final LoggerRepository loggerRepository;

  private LoggerServiceImpl() {
    loggerRepository = LoggerRepositoryImpl.getInstance();
  }

  @Override
  public Log createLog(Log log) {
    return loggerRepository.createLog(log);
  }

  @Override
  public List<Log> findLogs() {
    return loggerRepository.findLogs();
  }

  public static LoggerService getInstance() {
    return instance;
  }
}
