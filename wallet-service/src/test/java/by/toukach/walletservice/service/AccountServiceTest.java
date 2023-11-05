package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.CreateAccountDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.mapper.AccountMapper;
import by.toukach.walletservice.repository.AccountRepository;
import by.toukach.walletservice.service.account.impl.AccountServiceImpl;
import by.toukach.walletservice.service.user.UserService;
import by.toukach.walletservice.validator.Validator;
import by.toukach.walletservice.validator.impl.ParamValidator.Type;
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
public class AccountServiceTest extends BaseTest {

  @InjectMocks
  private AccountServiceImpl accountService;
  @Mock
  private AccountRepository accountRepository;
  @Mock
  private UserService userService;
  @Mock
  private Validator<AccountDto> accountDtoValidator;
  @Mock
  private Validator<CreateAccountDto> createAccountDtoValidator;
  @Mock
  private Validator<String> paramValidator;
  @Mock
  private AccountMapper accountMapper;
  private UserDto createdUser;
  private AccountDto newAccountDto;
  private AccountDto createdAccountDto;
  private AccountDto updatedAccountDto;
  private CreateAccountDto createAccountDto;
  private Account newAccount;
  private Account createdAccount;
  private Account updatedAccount;
  private List<Account> accountList;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    createdUser = getCreatedUserDto();
    newAccountDto = getNewAccountDto();
    createdAccountDto = getCreatedAccountDto();
    updatedAccountDto = getUpdatedAccountDto();
    createAccountDto = getCreateAccountDto();
    newAccount = getNewAccount();
    createdAccount = getCreatedAccount();
    updatedAccount = getUpdatedAccount();
    accountList = getAccountList();
  }

  @Test
  @DisplayName("Тест создания счета в приложении")
  public void createAccountTest_should_CreateAccount() {
    doNothing().when(createAccountDtoValidator).validate(createAccountDto);
    when(userService.findUserById(USER_ID)).thenReturn(createdUser);
    when(accountMapper.createAccountDtoToAccount(createAccountDto)).thenReturn(newAccount);
    when(accountRepository.createAccount(any())).thenReturn(createdAccount);
    when(accountMapper.accountToAccountDto(createdAccount)).thenReturn(createdAccountDto);

    AccountDto expectedResult = createdAccountDto;
    AccountDto actualResult = accountService.createAccount(createAccountDto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест создания счета в приложении для несуществующего пользователя")
  public void createAccountTest_should_ThrowError_WhenUserNotExist() {
    doNothing().when(createAccountDtoValidator).validate(createAccountDto);
    when(userService.findUserById(USER_ID)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> accountService.createAccount(createAccountDto))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест поиска счета в приложении по ID")
  public void findAccountByIdTest_should_FindAccount() {
    when(accountRepository.findAccountById(ACCOUNT_ID)).thenReturn(Optional.of(createdAccount));
    when(accountMapper.accountToAccountDto(createdAccount)).thenReturn(createdAccountDto);

    AccountDto expectedResult = createdAccountDto;
    AccountDto actualResult = accountService.findAccountById(ACCOUNT_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска счета в приложении по несуществующему ID")
  public void findAccountByIdTest_should_ThrowError_WhenAccountNotExist() {
    when(accountRepository.findAccountById(UN_EXISTING_ID))
        .thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> accountService.findAccountById(UN_EXISTING_ID))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест поиска счета в приложении по ID пользователя")
  public void findAccountsByUserIdTest_should_FindAccount() {
    doNothing().when(paramValidator)
        .validate(String.valueOf(USER_ID), Type.ID.name(), USER_ID_PARAM);
    when(accountRepository.findAccountsByUserId(USER_ID)).thenReturn(accountList);
    when(accountMapper.accountToAccountDto(createdAccount)).thenReturn(createdAccountDto);

    List<AccountDto> expectedResult = List.of(createdAccountDto);
    List<AccountDto> actualResult = accountService.findAccountsByUserId(USER_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска счета в приложении по несуществующему ID пользователя")
  public void findAccountsByUserIdTest_should_ThrowError_WhenUserNotExist() {
    when(accountRepository.findAccountsByUserId(UN_EXISTING_ID))
        .thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> accountService.findAccountsByUserId(UN_EXISTING_ID))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест обновления счета в приложении")
  public void updateAccountTest_should_UpdateAccount() {
    doNothing().when(accountDtoValidator).validate(updatedAccountDto);
    when(userService.isExists(USER_ID)).thenReturn(true);
    when(accountRepository.findAccountById(ACCOUNT_ID)).thenReturn(Optional.of(createdAccount));
    when(accountRepository.updateAccount(createdAccount)).thenReturn(Optional.of(updatedAccount));
    when(accountMapper.accountToAccountDto(updatedAccount)).thenReturn(updatedAccountDto);

    AccountDto expectedResult = updatedAccountDto;
    AccountDto actualResult = accountService.updateAccount(updatedAccountDto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест обновления несуществующего счета в приложении")
  public void updateAccountTest_should_ThrowError_WhenAccountNotExist() {
    when(accountRepository.findAccountById(UN_EXISTING_ID))
        .thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> accountService.updateAccount(updatedAccountDto))
        .isInstanceOf(EntityNotFoundException.class);
  }
}
