package by.toukach.logger.service.impl;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.entity.Log;
import by.toukach.logger.entity.mapper.LogMapper;
import by.toukach.logger.repository.LoggerRepository;
import by.toukach.logger.service.LoggerService;
import java.time.LocalDateTime;
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
    logDto.setCreatedAt(LocalDateTime.now());
    Log log = loggerRepository.createLog(logMapper.logDtoToLog(logDto));
    return logMapper.logToLogDto(log);
  }

  @Override
  public List<LogDto> findLogs() {
    return loggerRepository.findLogs().stream().map(logMapper::logToLogDto)
        .collect(Collectors.toList());
  }
}
