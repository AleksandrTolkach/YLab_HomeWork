package by.toukach.walletservice.controller.in.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.config.MainWebAppInitializer;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInResponseDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.service.AuthService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringJUnitWebConfig(classes = {MainWebAppInitializer.class})
public class AuthControllerTest extends BaseTest {

  private MockMvc mockMvc;
  @Mock
  private AuthService authService;
  private LogInDto logInDto;
  private SignUpDto signUpDto;
  private LogInResponseDto logInResponseDto;
  private UserDto userDto;

  @BeforeEach
  public void setUp() throws Exception {
    logInDto = getLogIn();
    signUpDto = getSignUp();
    logInResponseDto = getLogInDtoResponse();
    userDto = getCreatedUserDto();

    mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService)).build();
  }

  @Test
  @DisplayName("Тест входа в приложения")
  public void logInTest_should_SuccessfullyLogin() throws Exception{
    userDto.setPassword(null);
    userDto.setAccountList(null);

    when(authService.logIn(logInDto)).thenReturn(logInResponseDto);

    String logInJson = readJsonFile(LOG_IN_FILE_PATH);

    MvcResult mvcResult = mockMvc.perform(post(AUTH_LOG_IN_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(logInJson))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();

    UserDto expectedResult = userDto;
    UserDto actualResult =
        readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест регистрации в приложения")
  public void signUpTest_should_SuccessfullySignUp() throws Exception{
    userDto.setPassword(null);
    userDto.setAccountList(null);

    when(authService.signUp(signUpDto)).thenReturn(logInResponseDto);

    String logInJson = readJsonFile(SIGN_UP_FILE_PATH);

    MvcResult mvcResult = mockMvc.perform(post(AUTH_SING_UP_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(logInJson))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();

    UserDto expectedResult = userDto;
    UserDto actualResult =
        readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
