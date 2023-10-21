package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.toukach.walletservice.ContainersEnvironment;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.repository.impl.AccountRepositoryImpl;
import by.toukach.walletservice.repository.impl.MigrationImpl;
import by.toukach.walletservice.repository.impl.UserRepositoryImpl;
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

    migration = MigrationImpl.getInstance();
    migration.migrate();

    accountRepository = AccountRepositoryImpl.getInstance();
    userRepository = UserRepositoryImpl.getInstance();
    createdAccount = getCreatedAccount();
    newAccount = getNewAccount();
    updatedAccount = getUpdatedAccount();
    unExistingAccount = getUnExistingAccount();
    user = getNewUser();

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
    Account expectedResult = createdAccount;
    Account actualResult = accountRepository.findAccountById(ACCOUNT_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска счета в БД по несуществующему ID")
  public void findAccountByIdTest_should_ThrowError_WhenAccountNotFound() {
    assertThatThrownBy(() -> accountRepository.findAccountById(UN_EXISTING_ID))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест обновления счета в БД")
  public void updateAccountTest_should_UpdateAccount() {
    Account expectedAccount = updatedAccount;
    createdAccount.setSum(ACCOUNT_SUM + TRANSACTION_VALUE);
    Account actualResult = accountRepository.updateAccount(createdAccount);

    assertThat(actualResult).isEqualTo(expectedAccount);
  }

  @Test
  @DisplayName("Тест обновления несуществующего счета в БД")
  public void updateAccountTest_should_ThrowError_WhenAccountNotExist() {
    assertThatThrownBy(() -> accountRepository.updateAccount(unExistingAccount))
        .isInstanceOf(EntityNotFoundException.class);
  }
}
