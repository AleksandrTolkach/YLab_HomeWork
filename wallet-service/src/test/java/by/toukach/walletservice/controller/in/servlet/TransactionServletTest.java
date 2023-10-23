package by.toukach.walletservice.controller.in.servlet;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.InsufficientFundsException;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.handler.impl.CreditTransactionHandler;
import by.toukach.walletservice.service.handler.impl.DebitTransactionHandler;
import by.toukach.walletservice.service.handler.impl.TransactionHandlerFactoryImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.TransactionServiceImpl;
import by.toukach.walletservice.utils.JsonUtil;
import by.toukach.walletservice.validator.impl.ParamValidator;
import by.toukach.walletservice.validator.impl.ParamValidator.Type;
import by.toukach.walletservice.validator.impl.TransactionDtoValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
public class TransactionServletTest extends BaseTest {

  private TransactionServlet transactionServlet;
  @Mock
  private TransactionHandlerFactoryImpl transactionHandlerFactory;
  @Mock
  private CreditTransactionHandler creditTransactionHandler;
  @Mock
  private DebitTransactionHandler debitTransactionHandler;
  @Mock
  private TransactionServiceImpl transactionService;
  @Mock
  private ParamValidator paramValidator;
  @Mock
  private TransactionDtoValidator transactionDtoValidator;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  private MockedStatic<TransactionHandlerFactoryImpl> transactionHandlerFactoryMock;
  private MockedStatic<ParamValidator> paramValidatorMock;
  private MockedStatic<TransactionServiceImpl> transactionServiceMock;
  private MockedStatic<TransactionDtoValidator> transactionDtoValidatorMock;
  private PrintWriter writer;
  private BufferedReader readerWithCreditTransactionDto;
  private BufferedReader readerWithDebitTransactionDto;
  private BufferedReader readerWithUserId;
  private TransactionDto creditTransactionDto;
  private TransactionDto debitTransactionDto;

  @BeforeEach
  public void setUp() {
    creditTransactionDto = getCreditTransactionDto();
    debitTransactionDto = getDebitTransactionDto();

    transactionHandlerFactoryMock = mockStatic(TransactionHandlerFactoryImpl.class);
    transactionHandlerFactoryMock.when(TransactionHandlerFactoryImpl::getInstance)
        .thenReturn(transactionHandlerFactory);

    paramValidatorMock = mockStatic(ParamValidator.class);
    paramValidatorMock.when(ParamValidator::getInstance).thenReturn(paramValidator);

    transactionDtoValidatorMock = mockStatic(TransactionDtoValidator.class);
    transactionDtoValidatorMock.when(TransactionDtoValidator::getInstance)
        .thenReturn(transactionDtoValidator);

    transactionServiceMock = mockStatic(TransactionServiceImpl.class);
    transactionServiceMock.when(TransactionServiceImpl::getInstance).thenReturn(transactionService);

    writer = new PrintWriter(new StringWriter());
    readerWithCreditTransactionDto =
        new BufferedReader(new StringReader(JsonUtil.mapToJson(creditTransactionDto)));
    readerWithDebitTransactionDto =
        new BufferedReader(new StringReader(JsonUtil.mapToJson(debitTransactionDto)));

    transactionServlet = new TransactionServlet();
  }

  @AfterEach
  public void cleanUp() {
    transactionHandlerFactoryMock.close();
    paramValidatorMock.close();
    transactionDtoValidatorMock.close();
    transactionServiceMock.close();
  }

  @Test
  @DisplayName("Тест получение списка транзакций по ID пользователя")
  public void doGetTest_should_ReturnTransactions() throws IOException, ServletException {
    when(request.getParameter(USER_ID_PARAM)).thenReturn(String.valueOf(USER_ID));
    doNothing().when(paramValidator)
        .validate(String.valueOf(USER_ID), Type.ID.name(), USER_ID_PARAM);
    when(transactionService.findTransactionByUserId(USER_ID))
        .thenReturn(List.of(creditTransactionDto, debitTransactionDto));
    when(response.getWriter()).thenReturn(writer);

    transactionServlet.doGet(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  @DisplayName("Тест получение списка транзакций по несуществующему ID пользователя")
  public void doGetTest_should_ThrowError_WhenUserNotExist() throws IOException, ServletException {
    doNothing().when(paramValidator)
        .validate(String.valueOf(UN_EXISTING_ID), Type.ID.name(), USER_ID_PARAM);
    when(request.getParameter(USER_ID_PARAM)).thenReturn(String.valueOf(UN_EXISTING_ID));
    when(transactionService.findTransactionByUserId(UN_EXISTING_ID))
        .thenThrow(new EntityNotFoundException(
            String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, UN_EXISTING_ID)));

    transactionServlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_NOT_FOUND,
        String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, UN_EXISTING_ID));
  }

  @Test
  @DisplayName("Тест получение списка транзакций без передачи ID пользователя")
  public void doGetTest_should_ThrowError_WhenParamNotProvided()
      throws ServletException, IOException {
    doThrow(new ValidationExceptionList()).when(paramValidator)
        .validate(null, Type.ID.name(), USER_ID_PARAM);

    transactionServlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, new HashMap<>().toString());
  }

  @Test
  @DisplayName("Тест создания кредитной транзакции")
  public void doPostTest_should_CreateCreditTransaction() throws IOException, ServletException {
    when(request.getReader()).thenReturn(readerWithCreditTransactionDto);
    doNothing().when(transactionDtoValidator).validate(creditTransactionDto);
    when(transactionHandlerFactory
        .getHandler(TransactionType.CREDIT)).thenReturn(creditTransactionHandler);
    when(creditTransactionHandler.handle(creditTransactionDto)).thenReturn(creditTransactionDto);
    when(response.getWriter()).thenReturn(writer);

    transactionServlet.doPost(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_CREATED);
  }

  @Test
  @DisplayName("Тест создания дебетовой транзакции")
  public void doPostTest_should_DebitCreditTransaction() throws IOException, ServletException {
    when(request.getReader()).thenReturn(readerWithDebitTransactionDto);
    doNothing().when(transactionDtoValidator).validate(debitTransactionDto);
    when(transactionHandlerFactory
        .getHandler(TransactionType.DEBIT)).thenReturn(debitTransactionHandler);
    when(debitTransactionHandler.handle(debitTransactionDto)).thenReturn(debitTransactionDto);
    when(response.getWriter()).thenReturn(writer);

    transactionServlet.doPost(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_CREATED);
  }

  @Test
  @DisplayName("Тест создания дебетовой транзакции при недостатке средств")
  public void doPostTest_should_ThrowError_WhenPaymentRequired()
      throws IOException, ServletException {
    when(request.getReader()).thenReturn(readerWithDebitTransactionDto);
    doNothing().when(transactionDtoValidator).validate(debitTransactionDto);
    when(transactionHandlerFactory
        .getHandler(TransactionType.DEBIT)).thenReturn(debitTransactionHandler);
    when(debitTransactionHandler.handle(debitTransactionDto))
        .thenThrow(new InsufficientFundsException(ExceptionMessage.INSUFFICIENT_FUNDS));

    transactionServlet.doPost(request, response);

    response.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED,
        ExceptionMessage.INSUFFICIENT_FUNDS);
  }

  @Test
  @DisplayName("Тест создания дебетовой транзакции с некорректным форматом данных")
  public void doPostTest_should_ThrowError_WhenWrongDataFormat()
      throws IOException, ServletException {
    when(request.getReader()).thenReturn(readerWithDebitTransactionDto);
    doThrow(new ValidationExceptionList())
        .when(transactionDtoValidator).validate(debitTransactionDto);

    transactionServlet.doPost(request, response);

    response.sendError(HttpServletResponse.SC_BAD_REQUEST, new HashMap<>().toString());
  }
}
