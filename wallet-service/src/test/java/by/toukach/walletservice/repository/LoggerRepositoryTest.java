package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.ContainersEnvironment;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.repository.impl.LoggerRepositoryImpl;
import by.toukach.walletservice.repository.impl.MigrationImpl;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoggerRepositoryTest extends ContainersEnvironment {

  private LoggerRepository loggerRepository;
  private Migration migration;
  private Log newLog;
  private Log createdLog;

  @BeforeEach
  public void setUp() throws NoSuchFieldException, IllegalAccessException {
    injectTestJdbcUrl();

//    migration = MigrationImpl.getInstance();
    migration.migrate();

//    loggerRepository = LoggerRepositoryImpl.getInstance();
    newLog = getNewLog();
    createdLog = getCreatedLog();

    loggerRepository.createLog(getNewLog());
  }

  @AfterEach
  public void cleanUp() {
    migration.rollback(TAG_V_0_0);
  }

  @Test
  @DisplayName("Тест создания лога в БД")
  public void createLogTest_should_CreateLog() {
    Log expectedResult = createdLog;
    Log actualResult = loggerRepository.createLog(newLog);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска логов в БД")
  public void findLogsTest_should_FindLogs() {
    newLog.setId(FIRST_ENTITY_ID);

    List<Log> expectedResult = List.of(newLog);
    List<Log> actualResult = loggerRepository.findLogs();

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
