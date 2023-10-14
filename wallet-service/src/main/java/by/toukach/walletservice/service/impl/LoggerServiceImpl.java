package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.repository.LoggerRepository;
import by.toukach.walletservice.repository.impl.LoggerRepositoryImpl;
import by.toukach.walletservice.service.LoggerService;
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
