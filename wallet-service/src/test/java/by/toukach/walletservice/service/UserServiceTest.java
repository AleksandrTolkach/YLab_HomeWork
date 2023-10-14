package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.entity.converter.Converter;
import by.toukach.walletservice.entity.converter.impl.AccountConverter;
import by.toukach.walletservice.entity.converter.impl.UserConverter;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.repository.UserRepository;
import by.toukach.walletservice.repository.impl.UserRepositoryImpl;
import by.toukach.walletservice.service.impl.UserServiceImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
public class UserServiceTest extends BaseTest {

  private UserService userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private Converter<User, UserDto> userConverter;
  @Mock
  private Converter<Account, AccountDto> accountConverter;
  private MockedStatic<UserRepositoryImpl> userRepositoryMock;
  private MockedStatic<UserConverter> userConverterMock;
  private MockedStatic<AccountConverter> accountConverterMock;
  private UserDto newUserDto;
  private UserDto createdUserDto;
  private UserDto updatedUserDto;
  private User newUser;
  private User createdUser;
  private User updatedUser;
  private AccountDto accountDto;
  private Account account;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      IllegalArgumentException, InstantiationException, IllegalAccessException {
    newUserDto = getNewUserDto();
    createdUserDto = getCreatedUserDto();
    updatedUserDto = getUpdatedUserDto();
    newUser = getNewUserEntity();
    createdUser = getCreatedUserEntity();
    updatedUser = getUpdatedUserEntity();
    accountDto = getCreatedAccountDto();
    account = getCreatedAccountEntity();

    userRepositoryMock = mockStatic(UserRepositoryImpl.class);
    userRepositoryMock.when(UserRepositoryImpl::getInstance).thenReturn(userRepository);

    userConverterMock = mockStatic(UserConverter.class);
    userConverterMock.when(UserConverter::getInstance).thenReturn(userConverter);

    accountConverterMock = mockStatic(AccountConverter.class);
    accountConverterMock.when(AccountConverter::getInstance).thenReturn(accountConverter);

    Constructor<UserServiceImpl> privateConstructor = UserServiceImpl.class
        .getDeclaredConstructor();
    privateConstructor.setAccessible(true);

    userService = privateConstructor.newInstance();
  }

  @AfterEach
  public void cleanUp() {
    userRepositoryMock.close();
    userConverterMock.close();
    accountConverterMock.close();
    userService = null;
  }

  @Test
  @DisplayName("Тест создания пользователя в приложении")
  public void createUserTest_should_CreateUser() {
    when(userRepository.isExists(LOGIN)).thenReturn(false);
    when(userConverter.toEntity(newUserDto)).thenReturn(newUser);
    when(userRepository.createUser(newUser)).thenReturn(createdUser);
    when(userConverter.toDto(createdUser)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.createUser(newUserDto);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест создания пользователя в приложении с дублирующим логином")
  public void createUserTest_should_ThrowError_WhenLoginExists() {
    when(userRepository.isExists(LOGIN)).thenReturn(true);

    assertThatThrownBy(() -> userService.createUser(newUserDto))
        .isInstanceOf(EntityDuplicateException.class);
  }

  @Test
  @DisplayName("Тест поиска пользователя в приложении по ID")
  public void findUserByIdTest_should_FindUser() {
    when(userRepository.findUserById(USER_ID)).thenReturn(createdUser);
    when(userConverter.toDto(createdUser)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.findUserById(USER_ID);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя в приложении по несуществующему ID")
  public void findUserByIdTest_should_ThrowError_WhenUserNotExist() {
    when(userRepository.findUserById(UN_EXISTING_ID)).thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> userService.findUserById(UN_EXISTING_ID))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест поиска пользователя в приложении по логину")
  public void findUserByLoginTest_should_FindUser() {
    when(userRepository.findUserByLogin(LOGIN)).thenReturn(createdUser);
    when(userConverter.toDto(createdUser)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.findUserByLogin(LOGIN);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя в приложении по несуществующему логину")
  public void findUserByLoginTest_should_ThrowError_WhenUserNotExist() {
    when(userRepository.findUserByLogin(UN_EXISTING_LOGIN))
        .thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> userService.findUserByLogin(UN_EXISTING_LOGIN))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест обновления пользователя в приложении")
  public void updateUserTest_should_UpdateUser() {
    when(userRepository.findUserById(USER_ID)).thenReturn(createdUser);
    when(accountConverter.toEntity(accountDto)).thenReturn(account);
    when(userConverter.toDto(updatedUser)).thenReturn(updatedUserDto);

    UserDto expectedResult = updatedUserDto;
    createdUserDto.setAccountList(List.of(accountDto));
    UserDto actualResult = userService.updateUser(createdUserDto);

    assertThat(expectedResult).isEqualTo(actualResult);
  }

  @Test
  @DisplayName("Тест по обновлению несуществующего пользователя в приложении")
  public void updateUserTest_should_ThrowError_WhenUserNotFound() {
    UserDto createdUserDto = getCreatedUserDto();

    createdUserDto.setId(UN_EXISTING_ID);

    assertThatThrownBy(() -> userService.updateUser(createdUserDto))
        .isInstanceOf(EntityNotFoundException.class);
  }
}
