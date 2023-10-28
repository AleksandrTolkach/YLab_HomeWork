package by.toukach.walletservice.service.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.ArgumentValueException;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.security.SecurityContext;
import by.toukach.walletservice.service.handler.impl.CreditTransactionHandler;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.TransactionServiceImpl;
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
public class CreditTransactionHandlerTest extends BaseTest {

  private CreditTransactionHandler creditTransactionHandler;
  @Mock
  private TransactionServiceImpl transactionService;
  @Mock
  private AccountServiceImpl accountService;
  @Mock
  private LoggerServiceImpl loggerService;
  private MockedStatic<TransactionServiceImpl> transactionServiceMock;
  private MockedStatic<AccountServiceImpl> accountServiceMock;
  private MockedStatic<LoggerServiceImpl> loggerServiceMock;
  private MockedStatic<SecurityContext> securityContextMock;
  private TransactionDto transaction;
  private AccountDto accountDto;
  private Log newLog;
  private Log createdLog;
  private UserDto userDto;

  @BeforeEach
  public void setUp() {
    transaction = getCreditTransactionDto();
    accountDto = getCreatedAccountDto();
    newLog = getNewLog();
    createdLog = getCreatedLog();
    userDto = getCreatedUserDto();
    userDto.setAccountList(List.of(accountDto));

    transactionServiceMock = mockStatic(TransactionServiceImpl.class);
    transactionServiceMock.when(TransactionServiceImpl::getInstance).thenReturn(transactionService);

    accountServiceMock = mockStatic(AccountServiceImpl.class);
    accountServiceMock.when(AccountServiceImpl::getInstance).thenReturn(accountService);

    loggerServiceMock = mockStatic(LoggerServiceImpl.class);
    loggerServiceMock.when(LoggerServiceImpl::getInstance).thenReturn(loggerService);

    securityContextMock = mockStatic(SecurityContext.class);
    securityContextMock.when(SecurityContext::getCurrentUser).thenReturn(userDto);

    creditTransactionHandler = new CreditTransactionHandler();
  }

  @AfterEach
  public void cleanUp() {
    transactionServiceMock.close();
    accountServiceMock.close();
    loggerServiceMock.close();
    securityContextMock.close();
  }

  @Test
  @DisplayName("Тест обработки кредитной транзакции")
  public void handleTest_should_HandleTransaction() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction)).thenReturn(transaction);
    when(accountService.updateAccount(accountDto)).thenReturn(accountDto);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    TransactionDto expectedResult = transaction;
    TransactionDto actualResult = creditTransactionHandler.handle(transaction);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест обработки кредитной транзакции для несуществующего счета")
  public void handleTest_should_ThrowError_WhenAccountNotExist() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> creditTransactionHandler.handle(transaction))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест обработки кредитной транзакции с дублирующим ID")
  public void handleTest_should_ThrowError_WhenTransactionExists() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction))
        .thenThrow(EntityDuplicateException.class);

    assertThatThrownBy(() -> creditTransactionHandler.handle(transaction))
        .isInstanceOf(EntityDuplicateException.class);
  }

  @Test
  @DisplayName("Тест обработки кредитной транзакции с отрицательным значением суммы транзакции")
  public void handleTest_should_ThrowError_WhenPresentsNegativeArgument() {
    transaction.setValue(NEGATIVE_TRANSACTION_VALUE);

    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction)).thenReturn(transaction);

    assertThatThrownBy(() -> creditTransactionHandler.handle(transaction))
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
