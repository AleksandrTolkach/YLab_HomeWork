package by.toukach.walletservice.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.infrastructure.entity.Log;
import by.toukach.walletservice.infrastructure.repositories.impl.LoggerRepositoryImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoggerRepositoryTest extends BaseTest {

  @InjectMocks
  private LoggerRepositoryImpl loggerRepository;
  private Log newLog;
  private Log createdLog;

  @BeforeEach
  public void setUp() {
    newLog = getNewLog();
    createdLog = getCreatedLog();

    loggerRepository.createLog(getNewLog());
  }

  @Test
  public void createLogTest_should_CreateLog() {
    Log expectedResult = createdLog;
    Log actualResult = loggerRepository.createLog(newLog);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findLogsTest_should_FindLogs() {
    newLog.setId(FIRST_ENTITY_ID);

    List<Log> expectedResult = List.of(newLog);
    List<Log> actualResult = loggerRepository.findLogs();

    assertEquals(expectedResult, actualResult);
  }
}
