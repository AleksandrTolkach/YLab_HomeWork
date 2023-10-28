package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.ContainersEnvironment;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.repository.impl.AccountRepositoryImpl;
import by.toukach.walletservice.repository.impl.MigrationImpl;
import by.toukach.walletservice.repository.impl.UserRepositoryImpl;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountRepositoryTest extends ContainersEnvironment {

  private AccountRepository accountRepository;
  private UserRepository userRepository;
  private Migration migration;
  private Account createdAccount;
  private Account newAccount;
  private Account updatedAccount;
  private Account unExistingAccount;
  private User user;

  @BeforeEach
  public void setUp() throws NoSuchFieldException, IllegalAccessException {
    injectTestJdbcUrl();

    migration.migrate();

//    accountRepository = AccountRepositoryImpl.getInstance();
//    userRepository = UserRepositoryImpl.getInstance();

    createdAccount = getCreatedAccount();
    newAccount = getNewAccount();
    updatedAccount = getUpdatedAccount();
    unExistingAccount = getUnExistingAccount();
    user = getNewUserWithRole();

    userRepository.createUser(user);
    accountRepository.createAccount(newAccount);
  }

  @AfterEach
  public void cleanUp() {
    migration.rollback(TAG_V_0_0);
  }

  @Test
  @DisplayName("Тест сохранения счета в БД")
  public void createAccountTest_should_CreateAccount() {
    Account expectedResult = createdAccount;
    createdAccount.setId(SECOND_ENTITY_ID);
    Account actualResult = accountRepository.createAccount(newAccount);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска счета в БД по ID")
  public void findAccountByIdTest_should_FindAccount() {
    Optional<Account> expectedResult = Optional.of(createdAccount);
    Optional<Account> actualResult = accountRepository.findAccountById(ACCOUNT_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска счета в БД по несуществующему ID")
  public void findAccountByIdTest_should_ReturnEmptyOptional() {
    Optional<Account> expectedResult = Optional.empty();
    Optional<Account> actualResult = accountRepository.findAccountById(UN_EXISTING_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест обновления счета в БД")
  public void updateAccountTest_should_UpdateAccount() {
    Optional<Account> expectedAccount = Optional.of(updatedAccount);
    createdAccount.setSum(ACCOUNT_SUM.add(TRANSACTION_VALUE));
    Optional<Account> actualResult = accountRepository.updateAccount(createdAccount);

    assertThat(actualResult).isEqualTo(expectedAccount);
  }

  @Test
  @DisplayName("Тест обновления несуществующего счета в БД")
  public void updateAccountTest_should_ReturnEmptyOptional() {
    Optional<Account> expectedResult = Optional.empty();
    Optional<Account> actualResult = accountRepository.updateAccount(unExistingAccount);

    assertThat(actualResult).isEqualTo(expectedResult);
  }
}
