package by.toukach.walletservice.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.impl.AccountServiceImpl;
import by.toukach.walletservice.domain.services.impl.LoggerServiceImpl;
import by.toukach.walletservice.domain.services.impl.UserServiceImpl;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.entity.Log;
import by.toukach.walletservice.infrastructure.entity.converter.impl.AccountConverter;
import by.toukach.walletservice.infrastructure.repositories.impl.AccountRepositoryImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
  private AccountEntity newAccountEntity;
  private AccountEntity createdAccountEntity;
  private AccountEntity updatedAccountEntity;
  private UserDto updatedUserDto;
  private Log newLog;
  private Log createdLog;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    createdUser = getCreatedUserDto();
    newAccountDto = getNewAccountDto();
    createdAccountDto = getCreatedAccountDto();
    updatedAccountDto = getUpdatedAccountDto();
    newAccountEntity = getNewAccountEntity();
    createdAccountEntity = getCreatedAccountEntity();
    updatedAccountEntity = getUpdatedAccountEntity();
    updatedUserDto = getUpdatedUserDto();
    newLog = getNewLog();
    createdLog = getCreatedLog();

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
  public void createAccountTest_should_CreateAccount() {
    when(userService.findUserById(USER_ID)).thenReturn(createdUser);
    when(accountConverter.toEntity(newAccountDto)).thenReturn(newAccountEntity);
    when(accountRepository.createAccount(newAccountEntity)).thenReturn(createdAccountEntity);
    when(accountConverter.toDto(createdAccountEntity)).thenReturn(createdAccountDto);
    when(userService.updateUser(updatedUserDto)).thenReturn(updatedUserDto);
    when(loggerService.createLog(newLog)).thenReturn(createdLog);

    AccountDto expectedResult = createdAccountDto;
    AccountDto actualResult = accountService.createAccount(newAccountDto, USER_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void createAccountTest_should_ThrowError_WhenUserNotExist() {
    when(userService.findUserById(USER_ID)).thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class,
        () -> accountService.createAccount(newAccountDto, UN_EXISTING_ID));
  }

  @Test
  public void findAccountByIdTest_should_FindAccount() {
    when(accountRepository.findAccountById(ACCOUNT_ID)).thenReturn(createdAccountEntity);
    when(accountConverter.toDto(createdAccountEntity)).thenReturn(createdAccountDto);

    AccountDto expectedResult = createdAccountDto;
    AccountDto actualResult = accountService.findAccountById(ACCOUNT_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findAccountByIdTest_should_ThrowError_WhenAccountNotExist() {
    when(accountRepository.findAccountById(UN_EXISTING_ID))
        .thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class,
        () -> accountService.findAccountById(UN_EXISTING_ID));
  }

  @Test
  public void updateAccountTest_should_UpdateAccount() {
    when(accountRepository.findAccountById(ACCOUNT_ID)).thenReturn(createdAccountEntity);
    when(accountConverter.toDto(updatedAccountEntity)).thenReturn(updatedAccountDto);

    AccountDto expectedResult = updatedAccountDto;
    AccountDto actualResult = accountService.updateAccount(updatedAccountDto);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void updateAccountTest_should_ThrowError_WhenAccountNotExist() {
    when(accountRepository.findAccountById(UN_EXISTING_ID))
        .thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class,
        () -> accountService.updateAccount(updatedAccountDto));
  }
}
