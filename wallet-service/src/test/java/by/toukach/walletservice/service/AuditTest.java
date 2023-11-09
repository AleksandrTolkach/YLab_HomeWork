package by.toukach.walletservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.service.LoggerService;
import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.service.audit.impl.AuditServiceImpl;
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
public class AuditTest extends BaseTest {

  @InjectMocks
  private AuditServiceImpl auditService;
  @Mock
  private LoggerService loggerService;
  private List<LogDto> logListDto;

  @BeforeEach
  public void setUp() {
    logListDto = List.of(getCreatedLogDto());
  }

  @Test
  @DisplayName("Тест получения логов")
  public void findLogsTest_should_ReturnLogLost() {
    when(loggerService.findLogs()).thenReturn(logListDto);

    List<LogDto> expectedResult = logListDto;
    List<LogDto> actualResult = auditService.findLogs();

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
