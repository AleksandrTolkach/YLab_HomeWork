package by.toukach.walletservice.controller.in.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.UserDetailsArgumentResolver;
import by.toukach.walletservice.dto.CreateTransactionDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.service.transaction.TransactionHandler;
import by.toukach.walletservice.service.transaction.TransactionHandlerFactory;
import by.toukach.walletservice.service.transaction.TransactionService;
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
public class TransactionControllerTest extends BaseTest {

  private MockMvc mockMvc;
  @Mock
  private TransactionHandlerFactory transactionHandlerFactory;
  @Mock
  private TransactionHandler transactionHandler;
  @Mock
  private TransactionService transactionService;
  private TransactionDto creditTransactionDto;
  private CreateTransactionDto creditCreateTransactionDto;

  @BeforeEach
  public void setUp() throws Exception {
    creditTransactionDto = getCreditTransactionDto();
    creditCreateTransactionDto = getCreditCreateTransactionDto();

    mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionHandlerFactory,
        transactionService))
        .setCustomArgumentResolvers(new UserDetailsArgumentResolver())
        .build();
  }

  @Test
  @DisplayName("Тест поиска транзакций по ID пользователя")
  public void findTransactionsByUserIdTest_should_ReturnTransactions() throws Exception {
    when(transactionService.findTransactionByUserId(USER_ID))
        .thenReturn(List.of(creditTransactionDto));

    MvcResult mvcResult = mockMvc.perform(get(TRANSACTION_USER))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn();

    List<TransactionDto> expectedResult = List.of(creditTransactionDto);
    List<TransactionDto> actualResult =
        readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

    assertThat(actualResult).isEqualTo(expectedResult);
  }


  @Test
  @DisplayName("Тест создания транзакции")
  public void createTransactionTest_should_CreateTransaction() throws Exception {
    when(transactionHandlerFactory.getHandler(TransactionType.CREDIT))
        .thenReturn(transactionHandler);
    when(transactionHandler.handle(creditCreateTransactionDto)).thenReturn(creditTransactionDto);

    String creditTransactionJson = readJsonFile(CREDIT_TRANSACTION_FILE_PATH);

    MvcResult mvcResult = mockMvc.perform(post(TRANSACTION_URL)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(creditTransactionJson))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andReturn();

    TransactionDto expectedResult = creditTransactionDto;
    TransactionDto actualResult =
        readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
