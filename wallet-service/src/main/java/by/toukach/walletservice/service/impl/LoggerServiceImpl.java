package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.dto.LogDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.mapper.LogMapper;
import by.toukach.walletservice.repository.LoggerRepository;
import by.toukach.walletservice.service.LoggerService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Класс для выполнения операций с логами.
 * */
@Component
@RequiredArgsConstructor
public class LoggerServiceImpl implements LoggerService {

  private final LoggerRepository loggerRepository;
  private final LogMapper logMapper;

  @Override
  public LogDto createLog(LogDto logDto) {
    Log log = loggerRepository.createLog(logMapper.logDtoToLog(logDto));
    return logMapper.logToLogDto(log);
  }

  @Override
  public List<LogDto> findLogs() {
    return loggerRepository.findLogs().stream().map(logMapper::logToLogDto)
        .collect(Collectors.toList());
  }
}
