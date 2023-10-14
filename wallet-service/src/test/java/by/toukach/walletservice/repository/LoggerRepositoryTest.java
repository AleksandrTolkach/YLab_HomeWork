package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.repository.impl.LoggerRepositoryImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("Тест создания лога в памяти")
  public void createLogTest_should_CreateLog() {
    Log expectedResult = createdLog;
    Log actualResult = loggerRepository.createLog(newLog);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска логов в памяти")
  public void findLogsTest_should_FindLogs() {
    newLog.setId(FIRST_ENTITY_ID);

    List<Log> expectedResult = List.of(newLog);
    List<Log> actualResult = loggerRepository.findLogs();

    assertThat(expectedResult).isEqualTo(actualResult);
  }
}
