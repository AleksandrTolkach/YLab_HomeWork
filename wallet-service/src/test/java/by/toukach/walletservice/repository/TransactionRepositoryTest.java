package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.repository.impl.TransactionRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryTest extends BaseTest {

  @InjectMocks
  private TransactionRepositoryImpl transactionRepository;
  private Transaction transaction;

  @BeforeEach
  public void setUp() {
    transaction = getTransactionEntity();

    transactionRepository.createTransaction(transaction);
  }

  @Test
  @DisplayName("Тест сохранения транзакции в памяти")
  public void createTransactionTest_should_CreateTransaction() {
    Transaction expectedResult = transaction;
    Transaction actualResult = transactionRepository.createTransaction(
        transaction);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска транзакции в памяти по ID")
  public void findTransactionByIdTest_should_FindTransaction() {
    Transaction expectedResult = transaction;
    Transaction actualResult = transactionRepository.findTransactionById(TRANSACTION_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска транзакции в памяти по несуществующему ID")
  public void findTransactionByIdTest_should_ReturnNullIfTransactionNotFound() {
    Transaction expectedResult = null;
    Transaction actualResult = transactionRepository.findTransactionById(UN_EXISTING_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска транзакций в памяти по ID пользователя")
  public void findTransactionByUserIdTest_should_FindTransaction() {
    List<Transaction> expectedResult = List.of(transaction);
    List<Transaction> actualResult = transactionRepository.findTransactionByUserId(USER_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска транзакций в памяти по несуществующему ID пользователя")
  public void findTransactionByUserIdTest_should_ReturnEmptyListIfTransactionNotFound() {
    List<Transaction> expectedResult = new ArrayList<>();
    List<Transaction> actualResult =
        transactionRepository.findTransactionByUserId(UN_EXISTING_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест проверки существования транзакции в памяти по ID")
  public void isExistsTest_should_ReturnTrueIfTransactionExists() {
    boolean expectedResult = true;
    boolean actualResult = transactionRepository.isExists(TRANSACTION_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест проверки существования транзакции в памяти по несуществующему ID")
  public void isExistsTest_should_ReturnFalseIfTransactionNotExist() {
    boolean expectedResult = false;
    boolean actualResult = transactionRepository.isExists(UN_EXISTING_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }
}
