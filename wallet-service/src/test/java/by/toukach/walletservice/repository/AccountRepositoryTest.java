package by.toukach.walletservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.repository.impl.AccountRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryTest extends BaseTest {

  @InjectMocks
  private AccountRepositoryImpl accountRepository;
  private Account createdAccount;
  private Account newAccount;

  @BeforeEach
  public void setUp() {
    createdAccount = getCreatedAccountEntity();
    newAccount = getNewAccountEntity();

    accountRepository.createAccount(getNewAccountEntity());
  }

  @Test
  @DisplayName("Тест сохранения счета в памяти")
  public void createAccountTest_should_CreateAccount() {
    Account expectedResult = createdAccount;
    Account actualResult = accountRepository.createAccount(newAccount);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска счета в памяти по ID")
  public void findAccountByIdTest_should_FindAccount() {
    Account expectedResult = createdAccount;
    expectedResult.setId(FIRST_ENTITY_ID);
    Account actualResult = accountRepository.findAccountById(FIRST_ENTITY_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска счета в памяти по несуществующему ID")
  public void findAccountByIdTest_should_ReturnNullIfAccountNotFound() {
    Account expectedResult = null;
    Account actualResult = accountRepository.findAccountById(UN_EXISTING_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }
}
