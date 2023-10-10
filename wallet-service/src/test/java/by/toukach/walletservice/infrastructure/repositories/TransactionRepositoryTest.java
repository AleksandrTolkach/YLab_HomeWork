package by.toukach.walletservice.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.infrastructure.entity.TransactionEntity;
import by.toukach.walletservice.infrastructure.repositories.impl.TransactionRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryTest extends BaseTest {

  @InjectMocks
  private TransactionRepositoryImpl transactionRepository;
  private TransactionEntity transaction;

  @BeforeEach
  public void setUp() {
    transaction = getTransactionEntity();

    transactionRepository.createTransaction(transaction);
  }

  @Test
  public void createTransactionTest_should_CreateTransaction() {
    TransactionEntity expectedResult = transaction;
    TransactionEntity actualResult = transactionRepository.createTransaction(
        transaction);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findTransactionByIdTest_should_FindTransaction() {
    TransactionEntity expectedResult = transaction;
    TransactionEntity actualResult = transactionRepository.findTransactionById(TRANSACTION_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findTransactionByIdTest_should_ReturnNullIfTransactionNotFound() {
    TransactionEntity expectedResult = null;
    TransactionEntity actualResult = transactionRepository.findTransactionById(UN_EXISTING_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findTransactionByUserIdTest_should_FindTransaction() {
    List<TransactionEntity> expectedResult = List.of(transaction);
    List<TransactionEntity> actualResult = transactionRepository.findTransactionByUserId(USER_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findTransactionByUserIdTest_should_ReturnEmptyListIfTransactionNotFound() {
    List<TransactionEntity> expectedResult = new ArrayList<>();
    List<TransactionEntity> actualResult =
        transactionRepository.findTransactionByUserId(UN_EXISTING_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void isExistsTest_should_ReturnTrueIfTransactionExists() {
    boolean expectedResult = true;
    boolean actualResult = transactionRepository.isExists(TRANSACTION_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void isExistsTest_should_ReturnFalseIfTransactionNotExist() {
    boolean expectedResult = false;
    boolean actualResult = transactionRepository.isExists(UN_EXISTING_ID);

    assertEquals(expectedResult, actualResult);
  }
}
