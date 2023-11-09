package by.toukach.walletservice.controller.in.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.UserDetailsArgumentResolver;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.CreateAccountDto;
import by.toukach.walletservice.service.account.AccountService;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerTest extends BaseTest {

  private MockMvc mockMvc;
  @Mock
  private AccountService accountService;
  private AccountDto createdAccountDto;
  private CreateAccountDto createAccountDto;

  @BeforeEach
  public void setUp() throws Exception {
    createdAccountDto = getCreatedAccountDto();
    createAccountDto = getCreateAccountDto();

    mockMvc = MockMvcBuilders.standaloneSetup(new AccountController(accountService))
        .setCustomArgumentResolvers(new UserDetailsArgumentResolver())
        .build();
  }

  @Test
  @DisplayName("Тест получения счета по его ID")
  public void findAccountTest_should_ReturnAccount() throws Exception {
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(createdAccountDto);

    MvcResult mvcResult = mockMvc.perform(get(String.format(ACCOUNT_ID_URL, ACCOUNT_ID)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();

    AccountDto expectedResult = createdAccountDto;
    AccountDto actualResult =
        readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест получения счета по ID пользователя")
  public void findAccountsByUserIdTest_should_ReturnAccounts() throws Exception {
    when(accountService.findAccountsByUserId(USER_ID)).thenReturn(List.of(createdAccountDto));

    MvcResult mvcResult = mockMvc.perform(get(ACCOUNT_USER_URL))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();

    List<AccountDto> expectedResult = List.of(createdAccountDto);
    List<AccountDto> actualResult =
        readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест создания счета")
  public void createAccountTest_should_CreateAccount() throws Exception {
    when(accountService.createAccount(createAccountDto)).thenReturn(createdAccountDto);

    String accountJson = readJsonFile(CREATE_ACCOUNT_FILE_PATH);

    MvcResult mvcResult = mockMvc.perform(post(ACCOUNT_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(accountJson))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andReturn();

    AccountDto expectedResult = createdAccountDto;
    AccountDto actualResult =
        readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
