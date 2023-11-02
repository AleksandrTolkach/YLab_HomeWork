package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.ContainersEnvironment;
import by.toukach.walletservice.config.PropertyConfig;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.rowmapper.impl.LogRowMapper;
import by.toukach.walletservice.repository.impl.DbInitializerImpl;
import by.toukach.walletservice.repository.impl.LoggerRepositoryImpl;
import by.toukach.walletservice.repository.impl.MigrationImpl;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {MigrationImpl.class, DbInitializerImpl.class, PropertyConfig.class,
  LogRowMapper.class, LoggerRepositoryImpl.class})
public class LoggerRepositoryTest extends ContainersEnvironment {

  @Autowired
  private LoggerRepository loggerRepository;
  @Autowired
  private Migration migration;
  @Autowired
  private PropertyConfig propertyConfig;
  private Log log;

  @BeforeEach
  public void setUp() throws NoSuchFieldException, IllegalAccessException {
    propertyConfig.setDataBaseUrl(postgreSQLContainer.getJdbcUrl());
    migration.migrate();

    log = getNewLog();

    loggerRepository.createLog(getNewLog());
  }

  @AfterEach
  public void cleanUp() {
    migration.rollback(TAG_V_0_0);
  }

  @Test
  @DisplayName("Тест создания лога в БД")
  public void createLogTest_should_CreateLog() {
    Log expectedResult = log;
    Log actualResult = loggerRepository.createLog(log);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска логов в БД")
  public void findLogsTest_should_FindLogs() {
    log.setId(FIRST_ENTITY_ID);

    List<Log> expectedResult = List.of(log);
    List<Log> actualResult = loggerRepository.findLogs();

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
