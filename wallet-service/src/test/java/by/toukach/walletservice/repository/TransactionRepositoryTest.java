package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.ContainersEnvironment;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
public class TransactionRepositoryTest extends ContainersEnvironment {

  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private Migration migration;
  private Transaction transaction;
  private User user;
  private Account account;

  @BeforeEach
  public void setUp() throws NoSuchFieldException, IllegalAccessException {

    transaction = getTransaction();
    user = getNewUserWithRole();
    account = getNewAccount();

    userRepository.createUser(user);
    accountRepository.createAccount(account);
    transactionRepository.createTransaction(transaction);
  }

  @AfterEach
  public void cleanUp() {
    migration.rollback(TAG_V_0_0);
  }

  @Test
  @DisplayName("Тест сохранения транзакции в БД")
  public void createTransactionTest_should_CreateTransaction() {
    Transaction expectedResult = transaction;
    Transaction actualResult = transactionRepository.createTransaction(
        transaction);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска транзакции в БД по ID")
  public void findTransactionByIdTest_should_FindTransaction() {
    Optional<Transaction> expectedResult = Optional.of(transaction);
    Optional<Transaction> actualResult = transactionRepository.findTransactionById(TRANSACTION_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска транзакции в БД по несуществующему ID")
  public void findTransactionByIdTest_should_ReturnEmptyOptional() {
    Optional<Transaction> expectedResult = Optional.empty();
    Optional<Transaction> actualResult = transactionRepository.findTransactionById(
        UN_EXISTING_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска транзакций в БД по ID пользователя")
  public void findTransactionByUserIdTest_should_FindTransaction() {
    List<Transaction> expectedResult = List.of(transaction);
    List<Transaction> actualResult = transactionRepository.findTransactionByUserId(USER_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска транзакций в БД по несуществующему ID пользователя")
  public void findTransactionByUserIdTest_should_ReturnEmptyListIfTransactionNotFound() {
    List<Transaction> expectedResult = new ArrayList<>();
    List<Transaction> actualResult =
        transactionRepository.findTransactionByUserId(UN_EXISTING_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест проверки существования транзакции в БД по ID")
  public void isExistsTest_should_ReturnTrueIfTransactionExists() {
    boolean expectedResult = true;
    boolean actualResult = transactionRepository.isExists(TRANSACTION_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест проверки существования транзакции в БД по несуществующему ID")
  public void isExistsTest_should_ReturnFalseIfTransactionNotExist() {
    boolean expectedResult = false;
    boolean actualResult = transactionRepository.isExists(UN_EXISTING_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
