package by.toukach.walletservice.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.repositories.impl.AccountRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryTest extends BaseTest {

  @InjectMocks
  private AccountRepositoryImpl accountRepository;
  private AccountEntity createdAccount;
  private AccountEntity newAccount;

  @BeforeEach
  public void setUp() {
    createdAccount = getCreatedAccountEntity();
    newAccount = getNewAccountEntity();

    accountRepository.createAccount(getNewAccountEntity());
  }

  @Test
  public void createAccountTest_should_CreateAccount() {
    AccountEntity expectedResult = createdAccount;
    AccountEntity actualResult = accountRepository.createAccount(newAccount);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findAccountByIdTest_should_FindAccount() {
    AccountEntity expectedResult = createdAccount;
    expectedResult.setId(FIRST_ENTITY_ID);
    AccountEntity actualResult = accountRepository.findAccountById(FIRST_ENTITY_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findAccountByIdTest_should_ReturnNullIfAccountNotFound() {
    AccountEntity expectedResult = null;
    AccountEntity actualResult = accountRepository.findAccountById(UN_EXISTING_ID);

    assertEquals(expectedResult, actualResult);
  }
}
