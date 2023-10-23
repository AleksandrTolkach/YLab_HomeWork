package by.toukach.walletservice.controller.in.servlet;

import by.toukach.walletservice.dto.LogInDtoResponse;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.JsonMapperException;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.AuthService;
import by.toukach.walletservice.service.impl.AuthServiceImpl;
import by.toukach.walletservice.utils.CookieUtil;
import by.toukach.walletservice.utils.JsonUtil;
import by.toukach.walletservice.validator.Validator;
import by.toukach.walletservice.validator.impl.SignUpDtoValidator;
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
 * Сервлет для обработки HTTP запросов регистрации в приложении.
 */
@WebServlet("/auth/sign-up")
public class SignUpServlet extends HttpServlet {

  private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

  private final AuthService authService;
  private final Validator<SignUpDto> validator;

  public SignUpServlet() {
    authService = AuthServiceImpl.getInstance();
    validator = SignUpDtoValidator.getInstance();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    String requestString =
        req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

    SignUpDto signUpDto = null;
    try {
      signUpDto = JsonUtil.mapToPojo(requestString, SignUpDto.class);
    } catch (JsonMapperException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ExceptionMessage.WRONG_SIGN_UP);
      return;
    }

    LogInDtoResponse logInDtoResponse;
    try {
      logInDtoResponse = authService.signUp(signUpDto);
    } catch (ValidationExceptionList e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getErrorMessages().toString());
      return;
    } catch (EntityDuplicateException e) {
      resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
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

    resp.setStatus(HttpServletResponse.SC_CREATED);
  }
}
