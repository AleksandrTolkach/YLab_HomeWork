package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.mapper.AccountMapperImpl;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.repository.impl.AccountRepositoryImpl;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.UserServiceImpl;
import by.toukach.walletservice.validator.impl.AccountDtoValidator;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountServiceTest extends BaseTest {

  private AccountServiceImpl accountService;
  @Mock
  private AccountRepositoryImpl accountRepository;
  @Mock
  private UserServiceImpl userService;
  @Mock
  private LoggerServiceImpl loggerService;
  @Mock
  private AccountDtoValidator accountDtoValidator;
  @Mock
  private AccountMapperImpl accountMapper;
  private MockedStatic<UserServiceImpl> userServiceMock;
  private MockedStatic<AccountRepositoryImpl> accountRepositoryMock;
  private MockedStatic<LoggerServiceImpl> loggerServiceMock;
  private MockedStatic<AccountDtoValidator> accountDtoValidatorMock;
  private UserDto createdUser;
  private AccountDto newAccountDto;
  private AccountDto createdAccountDto;
  private AccountDto updatedAccountDto;
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
    newAccount = getNewAccount();
    createdAccount = getCreatedAccount();
    updatedAccount = getUpdatedAccount();
    accountList = getAccountList();

    userServiceMock = mockStatic(UserServiceImpl.class);
    userServiceMock.when(UserServiceImpl::getInstance).thenReturn(userService);

    accountRepositoryMock = mockStatic(AccountRepositoryImpl.class);
    accountRepositoryMock.when(AccountRepositoryImpl::getInstance).thenReturn(accountRepository);

    loggerServiceMock = mockStatic(LoggerServiceImpl.class);
    loggerServiceMock.when(LoggerServiceImpl::getInstance).thenReturn(loggerService);

    accountDtoValidatorMock = mockStatic(AccountDtoValidator.class);
    accountDtoValidatorMock.when(AccountDtoValidator::getInstance).thenReturn(accountDtoValidator);

    Constructor<AccountServiceImpl> privateConstructor = AccountServiceImpl.class
        .getDeclaredConstructor();
    privateConstructor.setAccessible(true);

    accountService = privateConstructor.newInstance();
  }

  @AfterEach
  public void cleanUp() {
    userServiceMock.close();
    accountRepositoryMock.close();
    loggerServiceMock.close();
    accountDtoValidatorMock.close();
  }

  @Test
  @DisplayName("Тест создания счета в приложении")
  public void createAccountTest_should_CreateAccount() {
    doNothing().when(accountDtoValidator).validate(newAccountDto);
    when(userService.findUserById(USER_ID)).thenReturn(createdUser);
    when(accountMapper.accountDtoToAccount(newAccountDto)).thenReturn(newAccount);
    when(accountRepository.createAccount(any())).thenReturn(createdAccount);
    when(accountMapper.accountToAccountDto(createdAccount)).thenReturn(createdAccountDto);

    AccountDto expectedResult = createdAccountDto;
    AccountDto actualResult = accountService.createAccount(newAccountDto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест создания счета в приложении для несуществующего пользователя")
  public void createAccountTest_should_ThrowError_WhenUserNotExist() {
    doNothing().when(accountDtoValidator).validate(newAccountDto);
    when(userService.findUserById(USER_ID)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> accountService.createAccount(newAccountDto))
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
    when(accountRepository.findAccountsByUserId(USER_ID)).thenReturn(accountList);

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
    when(userService.isExists(USER_ID)).thenReturn(true);
    when(accountRepository.findAccountById(ACCOUNT_ID)).thenReturn(Optional.of(createdAccount));
    when(accountRepository.updateAccount(createdAccount)).thenReturn(Optional.of(updatedAccount));
    when(accountMapper.accountToAccountDto(createdAccount)).thenReturn(createdAccountDto);

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
