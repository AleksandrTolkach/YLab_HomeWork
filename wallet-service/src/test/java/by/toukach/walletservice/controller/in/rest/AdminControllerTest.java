package by.toukach.walletservice.controller.in.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.toukach.logger.dto.LogDto;
import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.service.audit.AuditService;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerTest extends BaseTest {

  private MockMvc mockMvc;
  @Mock
  private AuditService auditService;
  private LogDto logDto;

  @BeforeEach
  public void setUp() throws Exception {
    logDto = getCreatedLogDto();

    mockMvc = MockMvcBuilders.standaloneSetup(new AdminController(auditService))
        .build();
  }

  @Test
  @DisplayName("Тест получения логов")
  public void findLogsTest_should_ReturnAccount() throws Exception {
    when(auditService.findLogs()).thenReturn(List.of(logDto));

    MvcResult mvcResult = mockMvc.perform(get(ADMIN_LOGS_URL))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();

    List<LogDto> expectedResult = List.of(logDto);
    List<LogDto> actualResult =
        readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
