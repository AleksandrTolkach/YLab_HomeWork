package by.toukach.walletservice.controller.in.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.LogInDtoResponse;
import by.toukach.walletservice.service.impl.AuthServiceImpl;
import by.toukach.walletservice.utils.CookieUtil;
import by.toukach.walletservice.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
public class LogOutServletTest extends BaseTest {

  private LogOutServlet logOutServlet;
  @Mock
  private AuthServiceImpl authService;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  private MockedStatic<AuthServiceImpl> authServiceMock;
  private String token;
  private Cookie[] cookies = new Cookie[1];
  private LogInDtoResponse logInDtoResponse;

  @BeforeEach
  public void setUp() throws IOException {
    token = JwtUtil.generateTokenFromUsername(LOGIN);
    cookies[0] = CookieUtil.generateAccessCookie(token);
    logInDtoResponse = getLogInDtoResponse();

    authServiceMock = mockStatic(AuthServiceImpl.class);
    authServiceMock.when(AuthServiceImpl::getInstance).thenReturn(authService);

    logOutServlet = new LogOutServlet();
  }

  @AfterEach
  public void cleanUp() {
    authServiceMock.close();
  }

  @Test
  @DisplayName("Проверка выхода из приложения")
  public void logOutTest_should_SuccessfullyLogOut() throws IOException {
    when(request.getCookies()).thenReturn(cookies);
    when(authService.logOut(LOGIN)).thenReturn(logInDtoResponse);

    logOutServlet.doPost(request, response);

    verify(response).addCookie(any());
    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
