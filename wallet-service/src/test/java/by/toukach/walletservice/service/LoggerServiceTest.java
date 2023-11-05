package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.entity.Log;
import by.toukach.logger.entity.mapper.LogMapper;
import by.toukach.logger.repository.LoggerRepository;
import by.toukach.logger.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.BaseTest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoggerServiceTest extends BaseTest {

  @InjectMocks
  private LoggerServiceImpl loggerService;
  @Mock
  private LoggerRepository loggerRepository;
  @Mock
  private LogMapper logMapper;
  private Log createdLog;
  private LogDto createdLogDto;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    createdLog = getCreatedLog();
    createdLogDto = getCreatedLogDto();
  }

  @Test
  @DisplayName("Тест создания лога в приложении")
  public void createLogTest_should_CreateLog() {
    when(logMapper.logDtoToLog(createdLogDto)).thenReturn(createdLog);
    when(loggerRepository.createLog(createdLog)).thenReturn(createdLog);
    when(logMapper.logToLogDto(createdLog)).thenReturn(createdLogDto);

    LogDto expectedResult = createdLogDto;
    LogDto actualResult = loggerService.createLog(createdLogDto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска логов")
  public void findLogsTest_should_FindLogs() {
    when(loggerRepository.findLogs()).thenReturn(List.of(createdLog));
    when(logMapper.logToLogDto(createdLog)).thenReturn(createdLogDto);

    List<LogDto> expectedResult = List.of(createdLogDto);
    List<LogDto> actualResult = loggerService.findLogs();

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
