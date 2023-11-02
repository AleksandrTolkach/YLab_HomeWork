package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.mapper.TransactionMapperImpl;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.repository.TransactionRepository;
import by.toukach.walletservice.service.impl.TransactionServiceImpl;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionServiceTest extends BaseTest {

  @InjectMocks
  private TransactionServiceImpl transactionService;
  @Mock
  private TransactionRepository transactionRepository;
  @Mock
  private UserService userService;
  @Mock
  private TransactionMapperImpl transactionMapper;
  private TransactionDto transactionDto;
  private Transaction transaction;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    transactionDto = getCreditTransactionDto();
    transaction = getTransaction();
  }

  @Test
  @DisplayName("Тест создания транзакции в приложении")
  public void createTransactionTest_should_CreateTransaction() {
    when(transactionMapper.transactionDtoToTransaction(transactionDto)).thenReturn(transaction);
    when(transactionRepository.createTransaction(any())).thenReturn(transaction);
    when(transactionMapper.transactionToTransactionDto(transaction)).thenReturn(transactionDto);

    TransactionDto expectedResult = transactionDto;
    TransactionDto actualResult = transactionService.createTransaction(transactionDto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска транзакции по Id")
  public void findTransactionByIdTest_should_FindTransaction() {
    when(transactionRepository.findTransactionById(TRANSACTION_ID))
        .thenReturn(Optional.of(transaction));
    when(transactionMapper.transactionToTransactionDto(transaction)).thenReturn(transactionDto);

    TransactionDto expectedResult = transactionDto;
    TransactionDto actualResult = transactionService.findTransactionById(TRANSACTION_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска транзакции по несуществующему ID")
  public void findTransactionByIdTest_should_ThrowError_WhenTransactionNotExist() {
    when(transactionRepository.findTransactionById(UN_EXISTING_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> transactionService.findTransactionById(UN_EXISTING_ID))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест поиска транзакций по ID пользователя")
  public void findTransactionByUserIdTest_should_FindTransactions() {
    when(userService.isExists(USER_ID)).thenReturn(true);
    when(transactionRepository.findTransactionByUserId(USER_ID))
        .thenReturn(List.of(transaction));
    when(transactionMapper.transactionToTransactionDto(transaction)).thenReturn(transactionDto);

    List<TransactionDto> expectedResult = List.of(transactionDto);
    List<TransactionDto> actualResult = transactionService.findTransactionByUserId(USER_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска транзакций по несуществующему ID пользователя")
  public void findTransactionByUserIdTest_should_ThrowError_WhenUserNotExist() {
    when(userService.isExists(UN_EXISTING_ID)).thenReturn(false);

    assertThatThrownBy(() -> transactionService.findTransactionByUserId(UN_EXISTING_ID))
        .isInstanceOf(EntityNotFoundException.class);
  }
}
