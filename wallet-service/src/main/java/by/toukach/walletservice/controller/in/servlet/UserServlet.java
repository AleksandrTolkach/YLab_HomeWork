package by.toukach.walletservice.controller.in.servlet;

import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.UserService;
import by.toukach.walletservice.service.impl.UserServiceImpl;
import by.toukach.walletservice.utils.JsonUtil;
import by.toukach.walletservice.validator.Validator;
import by.toukach.walletservice.validator.impl.ParamValidator;
import by.toukach.walletservice.validator.impl.ParamValidator.Type;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Сервлет для обработки HTTP запросов, связанных с пользователям.
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {

  private static final String ID = "id";
  private static final String LOGIN = "login";
  private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
  private static final String PARAMS = ID + "/" + LOGIN;

  private final UserService userService;
  private final Validator<String> paramValidator;

  public UserServlet() {
    userService = UserServiceImpl.getInstance();
    paramValidator = ParamValidator.getInstance();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String id = req.getParameter(ID);
    String login = req.getParameter(LOGIN);

    PrintWriter writer = resp.getWriter();
    UserDto userDto = null;

    String param;
    String value;
    try {
      paramValidator.validate(id, Type.ID.name(), ID);
      param = ID;
      value = id;
    } catch (ValidationExceptionList e) {
      try {
        paramValidator.validate(login, Type.STRING.name(), LOGIN);
        param = LOGIN;
        value = login;
      } catch (ValidationExceptionList e1) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
            String.format(ExceptionMessage.PARAMS_NOT_PROVIDED, PARAMS));
        return;
      }
    }

    try {
      userDto = findUserBy(param, value);
    } catch (EntityNotFoundException e) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
      return;
    } catch (DbException e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }

    writer.println(JsonUtil.mapToJson(userDto));

    resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    resp.setContentType(CONTENT_TYPE);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  private UserDto findUserBy(String param, String value) {
    if (param.equals(ID)) {
      return userService.findUserById(Long.parseLong(value));
    } else {
      return userService.findUserByLogin(value);
    }
  }
}
