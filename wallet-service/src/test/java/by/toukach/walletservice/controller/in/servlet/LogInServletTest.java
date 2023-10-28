package by.toukach.walletservice.controller.in.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInDtoResponse;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.impl.AuthServiceImpl;
import by.toukach.walletservice.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
public class LogInServletTest extends BaseTest {

  private LogInServlet logInServlet;
  @Mock
  private AuthServiceImpl authService;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  private MockedStatic<AuthServiceImpl> authServiceMock;
  private LogInDto logInDto;
  private LogInDtoResponse logInDtoResponse;
  private PrintWriter writer;
  private BufferedReader reader;

  @BeforeEach
  public void setUp() throws IOException {
    logInDto = getLogIn();
    logInDtoResponse = getLogInDtoResponse();
    writer = new PrintWriter(new StringWriter());
    reader = new BufferedReader(new StringReader(JsonUtil.mapToJson(logInDto)));

    authServiceMock = mockStatic(AuthServiceImpl.class);
    authServiceMock.when(AuthServiceImpl::getInstance).thenReturn(authService);

    logInServlet = new LogInServlet();
  }

  @AfterEach
  public void cleanUp() {
    authServiceMock.close();
  }

  @Test
  @DisplayName("Проверка входа в приложение")
  public void logInTest_should_SuccessfullyLogIn() throws IOException {
    when(request.getReader()).thenReturn(reader);
    when(authService.logIn(logInDto)).thenReturn(logInDtoResponse);
    when(response.getWriter()).thenReturn(writer);

    logInServlet.doPost(request, response);

    verify(response).addCookie(any());
    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  @DisplayName("Проверка входа в приложение с некорректными данными")
  public void logInTest_should_ThrowError_WhenParamIncorrect() throws IOException {
    when(request.getReader()).thenReturn(reader);
    when(authService.logIn(logInDto)).thenThrow(new ValidationExceptionList());

    logInServlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, new HashMap<>().toString());
  }

  @Test
  @DisplayName("Проверка входа в приложение под несуществующим логином")
  public void logInTest_should_ThrowError_WhenLoginNotExist() throws IOException {
    when(request.getReader()).thenReturn(reader);
    when(authService.logIn(logInDto))
        .thenThrow(new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND));

    logInServlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);
  }
}
