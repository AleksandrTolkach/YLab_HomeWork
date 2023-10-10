package by.toukach.walletservice.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.domain.services.impl.TransactionServiceImpl;
import by.toukach.walletservice.domain.services.impl.UserServiceImpl;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.infrastructure.entity.TransactionEntity;
import by.toukach.walletservice.infrastructure.entity.converter.impl.TransactionConverter;
import by.toukach.walletservice.infrastructure.repositories.impl.TransactionRepositoryImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
public class TransactionServiceTest extends BaseTest {

  private TransactionServiceImpl transactionService;
  @Mock
  private TransactionRepositoryImpl transactionRepository;
  @Mock
  private TransactionConverter transactionConverter;
  @Mock
  private UserService userService;
  private MockedStatic<TransactionRepositoryImpl> transactionRepositoryMock;
  private MockedStatic<TransactionConverter> transactionConverterMock;
  private MockedStatic<UserServiceImpl> userServiceMock;
  private TransactionDto transactionDto;
  private TransactionEntity transactionEntity;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    transactionDto = getTransactionDto();
    transactionEntity = getTransactionEntity();

    transactionRepositoryMock = mockStatic(TransactionRepositoryImpl.class);
    transactionRepositoryMock.when(TransactionRepositoryImpl::getInstance)
        .thenReturn(transactionRepository);

    transactionConverterMock = mockStatic(TransactionConverter.class);
    transactionConverterMock.when(TransactionConverter::getInstance)
        .thenReturn(transactionConverter);

    userServiceMock = mockStatic(UserServiceImpl.class);
    userServiceMock.when(UserServiceImpl::getInstance).thenReturn(userService);

    Constructor<TransactionServiceImpl> privateConstructor = TransactionServiceImpl.class
        .getDeclaredConstructor();
    privateConstructor.setAccessible(true);

    transactionService = privateConstructor.newInstance();
  }

  @AfterEach
  public void cleanUp() {
    transactionRepositoryMock.close();
    transactionConverterMock.close();
    userServiceMock.close();
  }

  @Test
  public void createTransactionTest_should_CreateTransaction() {
    when(transactionRepository.isExists(TRANSACTION_ID)).thenReturn(false);
    when(transactionConverter.toEntity(transactionDto)).thenReturn(transactionEntity);
    when(transactionRepository.createTransaction(transactionEntity)).thenReturn(transactionEntity);
    when(transactionConverter.toDto(transactionEntity)).thenReturn(transactionDto);

    TransactionDto expectedResult = transactionDto;
    TransactionDto actualResult = transactionService.createTransaction(transactionDto);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void createTransactionTest_should_ThrowError_WhenTransactionExists() {
    when(transactionRepository.isExists(TRANSACTION_ID)).thenReturn(true);

    assertThrows(EntityDuplicateException.class,
        () -> transactionService.createTransaction(transactionDto));
  }

  @Test
  public void findTransactionByIdTest_should_FindTransaction() {
    when(transactionRepository.findTransactionById(TRANSACTION_ID)).thenReturn(transactionEntity);
    when(transactionConverter.toDto(transactionEntity)).thenReturn(transactionDto);

    TransactionDto expectedResult = transactionDto;
    TransactionDto actualResult = transactionService.findTransactionById(TRANSACTION_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findTransactionByIdTest_should_ThrowError_WhenTransactionNotExist() {
    when(transactionRepository.findTransactionById(TRANSACTION_ID)).thenThrow(
        EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class,
        () -> transactionService.findTransactionById(TRANSACTION_ID));
  }

  @Test
  public void findTransactionByUserIdTest_should_FindTransactions() {
    when(userService.isExists(USER_ID)).thenReturn(true);
    when(transactionRepository.findTransactionByUserId(USER_ID))
        .thenReturn(List.of(transactionEntity));
    when(transactionConverter.toDto(transactionEntity)).thenReturn(transactionDto);

    List<TransactionDto> expectedResult = List.of(transactionDto);
    List<TransactionDto> actualResult = transactionService.findTransactionByUserId(USER_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findTransactionByUserIdTest_should_ThrowError_WhenUserNotExist() {
    when(userService.isExists(UN_EXISTING_ID)).thenReturn(false);

    assertThrows(EntityNotFoundException.class,
        () -> transactionService.findTransactionByUserId(UN_EXISTING_ID));
  }
}
