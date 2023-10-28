package by.toukach.walletservice.controller.in.servlet;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.JsonMapperException;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.AccountService;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервлет для обработки HTTP запросов, связанных с аккаунтом.
 */
@WebServlet("/account")
public class AccountServlet extends HttpServlet {

  private static final String ID = "id";
  private static final String USER_ID = "userId";
  private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
  private static final String PARAMS = ID + "/" + USER_ID;

  private final AccountService accountService;
  private final Validator<String> paramValidator;

  public AccountServlet() {
    accountService = AccountServiceImpl.getInstance();
    paramValidator = ParamValidator.getInstance();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String id = req.getParameter(ID);
    String userId = req.getParameter(USER_ID);

    String param;
    String value;
    try {
      paramValidator.validate(id, Type.ID.name(), ID);
      param = ID;
      value = id;
    } catch (ValidationExceptionList e) {
      try {
        paramValidator.validate(userId, Type.STRING.name(), USER_ID);
        param = USER_ID;
        value = userId;
      } catch (ValidationExceptionList e1) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
            String.format(ExceptionMessage.PARAMS_NOT_PROVIDED, PARAMS));
        return;
      }
    }

    List<AccountDto> accountDtoList;

    try {
      accountDtoList = findAccountBy(param, value);
    } catch (EntityNotFoundException e) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
      return;
    }

    List<String> accountDtoListJson = accountDtoList.stream()
        .map(JsonUtil::mapToJson)
        .collect(Collectors.toList());
    PrintWriter writer = resp.getWriter();
    writer.println(accountDtoListJson);

    resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    resp.setContentType(CONTENT_TYPE);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String requestString =
        req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

    AccountDto accountDto = null;
    try {
      accountDto = JsonUtil.mapToPojo(requestString, AccountDto.class);
    } catch (JsonMapperException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ExceptionMessage.WRONG_ACCOUNT);
      return;
    }

    try {
      accountDto = accountService.createAccount(accountDto);

    } catch (ValidationExceptionList e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getErrorMessages().toString());
      return;
    } catch (EntityNotFoundException e) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
      return;
    } catch (EntityDuplicateException e) {
      resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
      return;
    } catch (DbException e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
      return;
    }

    PrintWriter writer = resp.getWriter();
    writer.println(JsonUtil.mapToJson(accountDto));

    resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    resp.setContentType(CONTENT_TYPE);
    resp.setStatus(HttpServletResponse.SC_CREATED);
  }

  private List<AccountDto> findAccountBy(String param, String value) {
    if (param.equals(ID)) {
      return new ArrayList<>(List.of(accountService.findAccountById(Long.parseLong(value))));
    } else {
      return accountService.findAccountsByUserId(Long.parseLong(value));
    }
  }
}
