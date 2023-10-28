package by.toukach.walletservice.controller.in.servlet;

import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInDtoResponse;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.JsonMapperException;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.AuthService;
import by.toukach.walletservice.service.impl.AuthServiceImpl;
import by.toukach.walletservice.utils.CookieUtil;
import by.toukach.walletservice.utils.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Сервлет для обработки HTTP запросов входа в приложение.
 */
@WebServlet("/auth/login")
public class LogInServlet extends HttpServlet {

  private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

  private final AuthService authService;

  public LogInServlet() {
    authService = AuthServiceImpl.getInstance();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    String requestString =
        req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

    LogInDto logInDto = null;
    try {
      logInDto = JsonUtil.mapToPojo(requestString, LogInDto.class);
    } catch (JsonMapperException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ExceptionMessage.WRONG_LOG_IN);
      return;
    }

    LogInDtoResponse logInDtoResponse;
    try {
      logInDtoResponse = authService.logIn(logInDto);
    } catch (ValidationExceptionList e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getErrorMessages().toString());
      return;
    } catch (EntityNotFoundException e) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
      return;
    } catch (DbException e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
      return;
    }

    UserDto userDto = logInDtoResponse.getUserDto();

    PrintWriter writer = resp.getWriter();
    writer.println(JsonUtil.mapToJson(userDto));

    Cookie cookie = CookieUtil.generateAccessCookie(
        logInDtoResponse.getAuthentication().getToken());
    resp.addCookie(cookie);
    resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    resp.setContentType(CONTENT_TYPE);

    resp.setStatus(HttpServletResponse.SC_OK);
  }
}
