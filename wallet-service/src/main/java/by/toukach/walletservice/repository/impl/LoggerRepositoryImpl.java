package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.repository.LoggerRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для выполнения запросов, связанных с логами, в память.
 * */
public class LoggerRepositoryImpl implements LoggerRepository {

  private static final LoggerRepository instance = new LoggerRepositoryImpl();

  private final List<Log> logList = new ArrayList<>();
  private Long sequence = 0L;

  @Override
  public Log createLog(Log log) {
    log.setId(++sequence);
    logList.add(log);
    return log;
  }

  @Override
  public List<Log> findLogs() {
    return logList;
  }

  public static LoggerRepository getInstance() {
    return instance;
  }
}
