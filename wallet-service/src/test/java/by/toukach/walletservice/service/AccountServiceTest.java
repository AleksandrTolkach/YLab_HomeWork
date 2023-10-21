package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.converter.impl.AccountConverter;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.repository.impl.AccountRepositoryImpl;
import by.toukach.walletservice.service.impl.AccountServiceImpl;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
import by.toukach.walletservice.service.impl.UserServiceImpl;
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
  private AccountConverter accountConverter;
  @Mock
  private UserServiceImpl userService;
  @Mock
  private LoggerServiceImpl loggerService;
  private MockedStatic<UserServiceImpl> userServiceMock;
  private MockedStatic<AccountRepositoryImpl> accountRepositoryMock;
  private MockedStatic<AccountConverter> accountConverterMock;
  private MockedStatic<LoggerServiceImpl> loggerServiceMock;
  private UserDto createdUser;
  private AccountDto newAccountDto;
  private AccountDto createdAccountDto;
  private AccountDto updatedAccountDto;
  private Account newAccount;
  private Account createdAccount;
  private Account updatedAccount;
  private Log newLog;
  private Log createdLog;
  private List<AccountDto> accountDtoList;
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
    newLog = getNewLog();
    createdLog = getCreatedLog();
    accountDtoList = getAccountDtoList();
    accountList = getAccountList();

    userServiceMock = mockStatic(UserServiceImpl.class);
    userServiceMock.when(UserServiceImpl::getInstance).thenReturn(userService);

    accountRepositoryMock = mockStatic(AccountRepositoryImpl.class);
    accountRepositoryMock.when(AccountRepositoryImpl::getInstance).thenReturn(accountRepository);

    accountConverterMock = mockStatic(AccountConverter.class);
    accountConverterMock.when(AccountConverter::getInstance).thenReturn(accountConverter);

    loggerServiceMock = mockStatic(LoggerServiceImpl.class);
    loggerServiceMock.when(LoggerServiceImpl::getInstance).thenReturn(loggerService);

    Constructor<AccountServiceImpl> privateConstructor = AccountServiceImpl.class
        .getDeclaredConstructor();
    privateConstructor.setAccessible(true);

    accountService = privateConstructor.newInstance();
  }

  @AfterEach
  public void cleanUp() {
    userServiceMock.close();
    accountRepositoryMock.close();
    accountConverterMock.close();
    loggerServiceMock.close();
  }

  @Test
  @DisplayName("Тест создания счета в приложении")
  public void createAccountTest_should_CreateAccount() {
    when(userService.findUserById(USER_ID)).thenReturn(createdUser);
    when(accountConverter.toEntity(newAccountDto)).thenReturn(newAccount);
    when(accountRepository.createAccount(newAccount)).thenReturn(createdAccount);
    when(accountConverter.toDto(createdAccount)).thenReturn(createdAccountDto);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    AccountDto expectedResult = createdAccountDto;
    AccountDto actualResult = accountService.createAccount(newAccountDto);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест создания счета в приложении для несуществующего пользователя")
  public void createAccountTest_should_ThrowError_WhenUserNotExist() {
    when(userService.findUserById(USER_ID)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> accountService.createAccount(newAccountDto))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест поиска счета в приложении по ID")
  public void findAccountByIdTest_should_FindAccount() {
    when(accountRepository.findAccountById(ACCOUNT_ID)).thenReturn(Optional.of(createdAccount));
    when(accountConverter.toDto(createdAccount)).thenReturn(createdAccountDto);

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
    when(accountConverter.toDto(createdAccount)).thenReturn(createdAccountDto);

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
    when(accountConverter.toDto(updatedAccount)).thenReturn(updatedAccountDto);

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
