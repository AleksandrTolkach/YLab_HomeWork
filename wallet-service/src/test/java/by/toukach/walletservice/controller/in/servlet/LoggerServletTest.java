package by.toukach.walletservice.controller.in.servlet;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoggerServletTest extends BaseTest {

  private LoggerServlet loggerServlet;
  @Mock
  private LoggerServiceImpl loggerService;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  private MockedStatic<LoggerServiceImpl> loggerServiceMock;
  private PrintWriter writer;
  private Log log;

  @BeforeEach
  public void setUp() {
    log = getNewLog();

    writer = new PrintWriter(new StringWriter());

    loggerServiceMock = mockStatic(LoggerServiceImpl.class);
    loggerServiceMock.when(LoggerServiceImpl::getInstance).thenReturn(loggerService);

    loggerServlet = new LoggerServlet();
  }

  @AfterEach
  public void cleanUp() {
    loggerServiceMock.close();
  }

  @Test
  @DisplayName("Тест вывода логов")
  public void doGetTest_should_ReturnLogList() throws IOException {
    when(loggerService.findLogs()).thenReturn(List.of(log));
    when(response.getWriter()).thenReturn(writer);

    loggerServlet.doGet(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }
}
