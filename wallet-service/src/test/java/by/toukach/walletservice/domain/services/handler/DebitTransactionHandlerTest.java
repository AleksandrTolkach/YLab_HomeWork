package by.toukach.walletservice.domain.services.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.domain.services.handler.impl.DebitTransactionHandler;
import by.toukach.walletservice.domain.services.impl.AccountServiceImpl;
import by.toukach.walletservice.domain.services.impl.LoggerServiceImpl;
import by.toukach.walletservice.domain.services.impl.TransactionServiceImpl;
import by.toukach.walletservice.enumiration.TransactionType;
import by.toukach.walletservice.exceptions.ArgumentValueException;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.exceptions.InsufficientFundsException;
import by.toukach.walletservice.infrastructure.entity.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
  public void handleTest_should_HandleTransaction() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction)).thenReturn(transaction);
    when(accountService.updateAccount(accountDto)).thenReturn(accountDto);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    TransactionDto expectedTransactionResult = transaction;
    TransactionDto actualTransactionResult = debitTransactionHandler.handle(transaction);

    assertEquals(expectedTransactionResult, actualTransactionResult);
  }

  @Test
  public void handleTest_should_ThrowError_WhenNegativeBalance() {
    transaction.setValue(VALUE_BIG_AMOUNT);

    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);

    assertThrows(InsufficientFundsException.class,
        () -> debitTransactionHandler.handle(transaction));
  }

  @Test
  public void handleTest_should_ThrowError_WhenAccountNotExist() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class, () -> debitTransactionHandler.handle(transaction));
  }

  @Test
  public void handleTest_should_ThrowError_WhenTransactionExists() {
    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction))
        .thenThrow(EntityDuplicateException.class);

    assertThrows(EntityDuplicateException.class,
        () -> debitTransactionHandler.handle(transaction));
  }

  @Test
  public void handleTest_should_ThrowError_WhenPresentsNegativeArgument() {
    transaction.setValue(-TRANSACTION_VALUE);

    when(accountService.findAccountById(ACCOUNT_ID)).thenReturn(accountDto);
    when(transactionService.createTransaction(transaction)).thenReturn(transaction);

    assertThrows(ArgumentValueException.class, () -> debitTransactionHandler.handle(transaction));
  }

  @Test
  public void typeTest_should_ReturnType() {
    TransactionType expectedResult = TransactionType.DEBIT;
    TransactionType actualResult = debitTransactionHandler.type();

    assertEquals(expectedResult, actualResult);
  }
}
