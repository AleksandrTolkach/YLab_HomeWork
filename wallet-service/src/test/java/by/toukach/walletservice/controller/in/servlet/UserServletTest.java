package by.toukach.walletservice.controller.in.servlet;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.exception.ValidationExceptionList;
import by.toukach.walletservice.service.UserService;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.UserServiceImpl;
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
public class UserServletTest extends BaseTest {

  private UserServlet userServlet;
  @Mock
  private UserService userService;
  @Mock
  private ParamValidator paramValidator;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  private MockedStatic<UserServiceImpl> userServiceMock;
  private MockedStatic<ParamValidator> paramValidatorMock;
  private UserDto createdUserDto;
  private UserDto newUserDto;
  private PrintWriter writer;
  private BufferedReader reader;

  @BeforeEach
  public void setUp() {
    createdUserDto = getCreatedUserDto();
    newUserDto = getNewUserDtoWithRole();

    writer = new PrintWriter(new StringWriter());
    reader = new BufferedReader(new StringReader(JsonUtil.mapToJson(newUserDto)));

    userServiceMock = mockStatic(UserServiceImpl.class);
    userServiceMock.when(UserServiceImpl::getInstance).thenReturn(userService);

    paramValidatorMock = mockStatic(ParamValidator.class);
    paramValidatorMock.when(ParamValidator::getInstance).thenReturn(paramValidator);

    userServlet = new UserServlet();
  }

  @AfterEach
  public void cleanUp() {
    userServiceMock.close();
    paramValidatorMock.close();
  }

  @Test
  @DisplayName("Тест получения пользователя по его ID")
  public void doGetTest_should_ReturnUserById() throws ServletException, IOException {
    when(request.getParameter(ID_PARAM)).thenReturn(String.valueOf(USER_ID));
    when(request.getParameter(LOGIN_PARAM)).thenReturn(null);
    doNothing().when(paramValidator).validate(String.valueOf(USER_ID), Type.ID.name(), ID_PARAM);
    when(userService.findUserById(ACCOUNT_ID)).thenReturn(createdUserDto);
    when(response.getWriter()).thenReturn(writer);

    userServlet.doGet(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  @DisplayName("Тест получения пользователя по его логину")
  public void doGetTest_should_ReturnUserByLogin() throws ServletException, IOException {
    when(request.getParameter(ID_PARAM)).thenReturn(null);
    when(request.getParameter(LOGIN_PARAM)).thenReturn(LOGIN);
    doThrow(ValidationExceptionList.class).when(paramValidator)
        .validate(null, Type.ID.name(), ID_PARAM);
    doNothing().when(paramValidator).validate(LOGIN, Type.STRING.name(), LOGIN_PARAM);
    when(userService.findUserByLogin(LOGIN)).thenReturn(createdUserDto);
    when(response.getWriter()).thenReturn(writer);

    userServlet.doGet(request, response);

    verify(response).setCharacterEncoding(StandardCharsets.UTF_8.toString());
    verify(response).setContentType(CONTENT_TYPE);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  @DisplayName("Тест получения пользователя без передачи параметров")
  public void doGetTest_should_ThrowError_WhenParamsNotProvided()
      throws ServletException, IOException {
    when(request.getParameter(ID_PARAM)).thenReturn(null);
    when(request.getParameter(LOGIN_PARAM)).thenReturn(null);
    doThrow(new ValidationExceptionList()).when(paramValidator)
        .validate(null, Type.ID.name(), ID_PARAM);
    doThrow(new ValidationExceptionList()).when(paramValidator)
        .validate(null, Type.STRING.name(), LOGIN_PARAM);

    userServlet.doGet(request, response);

    verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, ID_LOGIN_NOT_PROVIDED);
  }
}
