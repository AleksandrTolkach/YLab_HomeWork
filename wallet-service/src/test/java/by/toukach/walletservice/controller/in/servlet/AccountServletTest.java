package by.toukach.walletservice.controller.in.servlet;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.utils.JsonUtil;
import by.toukach.walletservice.validator.impl.ParamValidator;
import by.toukach.walletservice.validator.impl.ParamValidator.Type;
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
public class AccountServletTest extends BaseTest {

  private AccountServlet accountServlet;
  @Mock
  private AccountServiceImpl accountService;
  @Mock
  private ParamValidator paramValidator;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  private MockedStatic<AccountServiceImpl> accountServiceMock;
  private MockedStatic<ParamValidator> paramValidatorMock;
  private AccountDto createdAccountDto;
  private AccountDto newAccountDto;
  private PrintWriter writer;
  private BufferedReader reader;
  private AccountDto wrongAccountDtoFormat;

  @BeforeEach
  public void setUp() {
    createdAccountDto = getCreatedAccountDto();
    newAccountDto = getNewAccountDto();
    wrongAccountDtoFormat = getWrongAccountDtoFormat();

    writer = new PrintWriter(new StringWriter());
    reader = new BufferedReader(new StringReader(JsonUtil.mapToJson(newAccountDto)));

    accountServiceMock = mockStatic(AccountServiceImpl.class);
    accountServiceMock.when(AccountServiceImpl::getInstance).thenReturn(accountService);

    paramValidatorMock = mockStatic(ParamValidator.class);
    paramValidatorMock.when(ParamValidator::getInstance).thenReturn(paramValidator);

    accountServlet = new AccountServlet();
  }

  @AfterEach
  public void cleanUp() {
    accountServiceMock.close();
    paramValidatorMock.close();
  }

  @Test
  @DisplayName("Тест получение счета по его ID")
  public void doGetTest_should_ReturnAccountById() throws ServletException, IOException {
    when(request.getParameter(ID_PARAM)).thenReturn(String.valueOf(ACCOUNT_ID));
    doNothing().when(paramValidator).validate(String.valueOf(ACCOUNT_ID), Type.ID.name(), ID_PARAM);
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(createdAccountDto);
    when(response.getWriter()).thenReturn(writer);

    accountServlet.doGet(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  @DisplayName("Тест получение счета по ID пользователя")
  public void doGetTest_should_ReturnAccountByUserId() throws ServletException, IOException {
    when(request.getParameter(ID_PARAM)).thenReturn(null);
    when(request.getParameter(USER_ID_PARAM)).thenReturn(String.valueOf(USER_ID));
    doThrow(ValidationExceptionList.class).when(paramValidator)
        .validate(null, Type.ID.name(), ID_PARAM);
    doNothing().when(paramValidator)
        .validate(String.valueOf(USER_ID), Type.STRING.name(), ID_PARAM);
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(createdAccountDto);
    when(response.getWriter()).thenReturn(writer);

    accountServlet.doGet(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  @DisplayName("Тест получение счета без передачи параметров")
  public void doGetTest_should_ThrowError_WhenParamsNotPresent()
      throws ServletException, IOException {
    when(request.getParameter(USER_ID_PARAM)).thenReturn(null);
    when(request.getParameter(USER_ID_PARAM)).thenReturn(null);
    doThrow(ValidationExceptionList.class).when(paramValidator)
        .validate(null, Type.ID.name(), ID_PARAM);
    doThrow(ValidationExceptionList.class).when(paramValidator)
        .validate(null, Type.STRING.name(), USER_ID_PARAM);

    accountServlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, ID_USER_ID_NOT_PROVIDED);
  }

  @Test
  @DisplayName("Тест создания счета")
  public void doPostTest_should_CreateAccount() throws ServletException, IOException {
    when(request.getReader()).thenReturn(reader);
    when(accountService.createAccount(newAccountDto)).thenReturn(createdAccountDto);
    when(response.getWriter()).thenReturn(writer);

    accountServlet.doPost(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_CREATED);
  }

  @Test
  @DisplayName("Тест создания счета с неверным форматом данных")
  public void doPostTest_should_ThrowError_WhenWrongAccountFormat()
      throws ServletException, IOException {
    reader = new BufferedReader(new StringReader(JsonUtil.mapToJson(wrongAccountDtoFormat)));
    when(request.getReader()).thenReturn(reader);
    when(accountService.createAccount(wrongAccountDtoFormat))
        .thenThrow(new ValidationExceptionList());
    when(response.getWriter()).thenReturn(writer);

    accountServlet.doPost(request, response);

    verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, new HashMap<>().toString());
  }
}
