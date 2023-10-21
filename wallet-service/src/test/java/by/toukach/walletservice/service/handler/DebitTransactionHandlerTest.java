package by.toukach.walletservice.service.handler;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exception.ArgumentValueException;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.InsufficientFundsException;
import by.toukach.walletservice.service.handler.impl.DebitTransactionHandler;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.TransactionServiceImpl;
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
public class DebitTransactionHandlerTest extends BaseTest {

  private DebitTransactionHandler debitTransactionHandler;
  @Mock
  private TransactionServiceImpl transactionService;
  @Mock
  private AccountServiceImpl accountService;
  @Mock
  private LoggerServiceImpl loggerService;
  private MockedStatic<TransactionServiceImpl> transactionServiceMock;
  private MockedStatic<AccountServiceImpl> accountServiceMock;
  private MockedStatic<LoggerServiceImpl> loggerServiceMock;
  private TransactionDto transaction;
  private AccountDto accountDto;
  private Log newLog;
  private Log createdLog;

  @BeforeEach
  public void setUp() {
    transaction = getTransactionDto();
    accountDto = getCreatedAccountDto();
    newLog = getNewLog();
    createdLog = getCreatedLog();

    transactionServiceMock = mockStatic(TransactionServiceImpl.class);
    transactionServiceMock.when(TransactionServiceImpl::getInstance).thenReturn(transactionService);

    accountServiceMock = mockStatic(AccountServiceImpl.class);
    accountServiceMock.when(AccountServiceImpl::getInstance).thenReturn(accountService);

    loggerServiceMock = mockStatic(LoggerServiceImpl.class);
    loggerServiceMock.when(LoggerServiceImpl::getInstance).thenReturn(loggerService);

    debitTransactionHandler = new DebitTransactionHandler();
  }

  @AfterEach
  public void cleanUp() {
    transactionServiceMock.close();
    accountServiceMock.close();
    loggerServiceMock.close();
  }

  @Test
  @DisplayName("Тест обработки дебитной транзакции")
  public void handleTest_should_HandleTransaction() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction)).thenReturn(transaction);
    when(accountService.updateAccount(accountDto)).thenReturn(accountDto);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    TransactionDto expectedResult = transaction;
    TransactionDto actualResult = debitTransactionHandler.handle(transaction);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест обработки дебитной транзакции при недостатке средств на счете")
  public void handleTest_should_ThrowError_WhenNegativeBalance() {
    transaction.setValue(VALUE_BIG_AMOUNT);

    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);

    assertThatThrownBy(() -> debitTransactionHandler.handle(transaction))
        .isInstanceOf(InsufficientFundsException.class);
  }

  @Test
  @DisplayName("Тест обработки дебитной транзакции для несуществующего счета")
  public void handleTest_should_ThrowError_WhenAccountNotExist() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> debitTransactionHandler.handle(transaction))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест обработки дебитной транзакции с дублирующим ID")
  public void handleTest_should_ThrowError_WhenTransactionExists() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction))
        .thenThrow(EntityDuplicateException.class);

    assertThatThrownBy(() -> debitTransactionHandler.handle(transaction))
        .isInstanceOf(EntityDuplicateException.class);
  }

  @Test
  @DisplayName("Тест обработки дебитной транзакции с отрицательным значением суммы транзакции")
  public void handleTest_should_ThrowError_WhenPresentsNegativeArgument() {
    transaction.setValue(NEGATIVE_TRANSACTION_VALUE);

    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction)).thenReturn(transaction);

    assertThatThrownBy(() -> debitTransactionHandler.handle(transaction))
        .isInstanceOf(ArgumentValueException.class);
  }

  @Test
  @DisplayName("Тест вывода типа обработчика транзакций")
  public void typeTest_should_ReturnType() {
    TransactionType expectedResult = TransactionType.DEBIT;
    TransactionType actualResult = debitTransactionHandler.type();

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
