package by.toukach.walletservice.controller.in.servlet;

import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.EntityConflictException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.InsufficientFundsException;
import by.toukach.walletservice.exception.JsonMapperException;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.TransactionService;
import by.toukach.walletservice.service.handler.TransactionHandler;
import by.toukach.walletservice.service.handler.TransactionHandlerFactory;
import by.toukach.walletservice.service.handler.impl.TransactionHandlerFactoryImpl;
import by.toukach.walletservice.service.impl.TransactionServiceImpl;
import by.toukach.walletservice.utils.JsonUtil;
import by.toukach.walletservice.validator.Validator;
import by.toukach.walletservice.validator.impl.ParamValidator;
import by.toukach.walletservice.validator.impl.ParamValidator.Type;
import by.toukach.walletservice.validator.impl.TransactionDtoValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервлет для обработки HTTP запросов, связанных с транзакциями.
 */
@WebServlet("/transaction")
public class TransactionServlet extends HttpServlet {

  private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
  private static final String USER_ID = "userId";

  private final TransactionHandlerFactory transactionHandlerFactory;
  private final TransactionService transactionService;
  private final Validator<TransactionDto> validator;
  private final Validator<String> paramValidator;

  /**
   * Конструктор для создания сервлета для работы с запросами, связанными с транзакциями.
   */
  public TransactionServlet() {
    transactionHandlerFactory = TransactionHandlerFactoryImpl.getInstance();
    validator = TransactionDtoValidator.getInstance();
    transactionService = TransactionServiceImpl.getInstance();
    paramValidator = ParamValidator.getInstance();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String userId = req.getParameter(USER_ID);

    try {
      paramValidator.validate(userId, Type.ID.name(), USER_ID);
    } catch (ValidationExceptionList e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getErrorMessages().toString());
      return;
    }

    List<TransactionDto> transactionDtoList = null;
    try {
      transactionDtoList = transactionService.findTransactionByUserId(Long.parseLong(userId));
    } catch (EntityNotFoundException e) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
      return;
    } catch (DbException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
      return;
    }

    PrintWriter writer = resp.getWriter();
    List<String> transactionDtoListJson = transactionDtoList.stream()
        .map(JsonUtil::mapToJson)
        .collect(Collectors.toList());
    writer.println(transactionDtoListJson);

    resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    resp.setContentType(CONTENT_TYPE);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String requestString =
        req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

    TransactionDto transactionDto = null;
    try {
      transactionDto = JsonUtil.mapToPojo(requestString, TransactionDto.class);
    } catch (JsonMapperException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ExceptionMessage.WRONG_TRANSACTION);
      return;
    }

    try {
      validator.validate(transactionDto);
    } catch (ValidationExceptionList e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getErrorMessages().toString());
      return;
    }

    TransactionHandler handler = transactionHandlerFactory.getHandler(transactionDto.getType());

    try {
      transactionDto = handler.handle(transactionDto);

    } catch (EntityNotFoundException e) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
      return;
    } catch (InsufficientFundsException e) {
      resp.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, e.getMessage());
      return;
    } catch (DbException | EntityConflictException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
      return;
    }

    PrintWriter writer = resp.getWriter();
    writer.println(JsonUtil.mapToJson(transactionDto));

    resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    resp.setContentType(CONTENT_TYPE);
    resp.setStatus(HttpServletResponse.SC_CREATED);
  }
}
