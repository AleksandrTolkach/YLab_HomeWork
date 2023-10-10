package by.toukach.walletservice.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.services.impl.LoggerServiceImpl;
import by.toukach.walletservice.infrastructure.entity.Log;
import by.toukach.walletservice.infrastructure.repositories.LoggerRepository;
import by.toukach.walletservice.infrastructure.repositories.impl.LoggerRepositoryImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoggerServiceTest extends BaseTest {

  private LoggerService loggerService;
  @Mock
  private LoggerRepository loggerRepository;
  private MockedStatic<LoggerRepositoryImpl> loggerRepositoryMock;
  private Log newLog;
  private Log createdLog;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    newLog = getNewLog();
    createdLog = getCreatedLog();

    loggerRepositoryMock = mockStatic(LoggerRepositoryImpl.class);
    loggerRepositoryMock.when(LoggerRepositoryImpl::getInstance).thenReturn(loggerRepository);

    Constructor<LoggerServiceImpl> privateConstructor = LoggerServiceImpl.class
        .getDeclaredConstructor();
    privateConstructor.setAccessible(true);

    loggerService = privateConstructor.newInstance();
  }

  @AfterEach
  public void cleanUp() {
    loggerRepositoryMock.close();
  }

  @Test
  public void createLogTest_should_CreateLog() {
    when(loggerRepository.createLog(newLog)).thenReturn(createdLog);

    Log expectedResult = createdLog;
    Log actualResult = loggerService.createLog(newLog);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findLogsTest_should_FindLogs() {
    when(loggerRepository.findLogs()).thenReturn(List.of(createdLog));

    List<Log> expectedResult = List.of(createdLog);
    List<Log> actualResult = loggerService.findLogs();

    assertEquals(expectedResult, actualResult);
  }
}
