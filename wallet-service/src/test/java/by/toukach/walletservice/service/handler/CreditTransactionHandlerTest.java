package by.toukach.walletservice.service.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.CreateTransactionDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.ArgumentValueException;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.service.account.AccountService;
import by.toukach.walletservice.service.transaction.TransactionService;
import by.toukach.walletservice.service.transaction.impl.CreditTransactionHandler;
import by.toukach.walletservice.validator.Validator;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CreditTransactionHandlerTest extends BaseTest {

  @InjectMocks
  private CreditTransactionHandler creditTransactionHandler;
  @Mock
  private TransactionService transactionService;
  @Mock
  private AccountService accountService;
  @Mock
  private Validator<TransactionDto> transactionDtoValidator;
  private MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic;
  @Mock
  private SecurityContext securityContext;
  @Mock
  private Authentication authentication;
  private TransactionDto transactionDto;
  private CreateTransactionDto createTransactionDto;
  private AccountDto accountDto;
  private UserDto userDto;
  private UserDetails userDetails;

  @BeforeEach
  public void setUp() {
    securityContextHolderMockedStatic = mockStatic(SecurityContextHolder.class);

    transactionDto = getCreditTransactionDto();
    createTransactionDto = getCreditCreateTransactionDto();
    accountDto = getCreatedAccountDto();
    userDto = getCreatedUserDto();
    userDto.setAccountList(List.of(accountDto));
    userDetails = getUserDetails();
  }

  @AfterEach
  public void cleanUp() {
    securityContextHolderMockedStatic.close();
  }

  @Test
  @DisplayName("Тест обработки кредитной транзакции")
  public void handleTest_should_HandleTransaction() {
    doNothing().when(transactionDtoValidator).validate(transactionDto);
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    securityContextHolderMockedStatic.when(SecurityContextHolder::getContext)
        .thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(transactionService.createTransaction(createTransactionDto)).thenReturn(transactionDto);
    when(accountService.updateAccount(accountDto)).thenReturn(accountDto);

    TransactionDto expectedResult = transactionDto;
    TransactionDto actualResult = creditTransactionHandler.handle(createTransactionDto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест обработки кредитной транзакции для несуществующего счета")
  public void handleTest_should_ThrowError_WhenAccountNotExist() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> creditTransactionHandler.handle(createTransactionDto))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест обработки кредитной транзакции с дублирующим ID")
  public void handleTest_should_ThrowError_WhenTransactionExists() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    securityContextHolderMockedStatic.when(SecurityContextHolder::getContext)
        .thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(transactionService.createTransaction(createTransactionDto))
        .thenThrow(EntityDuplicateException.class);

    assertThatThrownBy(() -> creditTransactionHandler.handle(createTransactionDto))
        .isInstanceOf(EntityDuplicateException.class);
  }

  @Test
  @DisplayName("Тест обработки кредитной транзакции с отрицательным значением суммы транзакции")
  public void handleTest_should_ThrowError_WhenPresentsNegativeArgument() {
    createTransactionDto.setValue(NEGATIVE_TRANSACTION_VALUE);

    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    securityContextHolderMockedStatic.when(SecurityContextHolder::getContext)
        .thenReturn(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(transactionService.createTransaction(createTransactionDto)).thenReturn(transactionDto);
    assertThatThrownBy(() -> creditTransactionHandler.handle(createTransactionDto))
        .isInstanceOf(ArgumentValueException.class);
  }

  @Test
  @DisplayName("Тест вывода типа обработчика транзакций")
  public void typeTest_should_ReturnType() {
    TransactionType expectedResult = TransactionType.CREDIT;
    TransactionType actualResult = creditTransactionHandler.type();

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
