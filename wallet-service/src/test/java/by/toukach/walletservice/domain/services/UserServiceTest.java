package by.toukach.walletservice.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.impl.UserServiceImpl;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.entity.UserEntity;
import by.toukach.walletservice.infrastructure.entity.converter.Converter;
import by.toukach.walletservice.infrastructure.entity.converter.impl.AccountConverter;
import by.toukach.walletservice.infrastructure.entity.converter.impl.UserConverter;
import by.toukach.walletservice.infrastructure.repositories.UserRepository;
import by.toukach.walletservice.infrastructure.repositories.impl.UserRepositoryImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
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
public class UserServiceTest extends BaseTest {

  private UserService userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private Converter<UserEntity, UserDto> userConverter;
  @Mock
  private Converter<AccountEntity, AccountDto> accountConverter;
  private MockedStatic<UserRepositoryImpl> userRepositoryMock;
  private MockedStatic<UserConverter> userConverterMock;
  private MockedStatic<AccountConverter> accountConverterMock;
  private UserDto newUserDto;
  private UserDto createdUserDto;
  private UserDto updatedUserDto;
  private UserEntity newUserEntity;
  private UserEntity createdUserEntity;
  private UserEntity updatedUserEntity;
  private AccountDto accountDto;
  private AccountEntity accountEntity;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      IllegalArgumentException, InstantiationException, IllegalAccessException {
    newUserDto = getNewUserDto();
    createdUserDto = getCreatedUserDto();
    updatedUserDto = getUpdatedUserDto();
    newUserEntity = getNewUserEntity();
    createdUserEntity = getCreatedUserEntity();
    updatedUserEntity = getUpdatedUserEntity();
    accountDto = getCreatedAccountDto();
    accountEntity = getCreatedAccountEntity();

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
  public void createUserTest_should_CreateUser() {
    when(userRepository.isExists(LOGIN)).thenReturn(false);
    when(userConverter.toEntity(newUserDto)).thenReturn(newUserEntity);
    when(userRepository.createUser(newUserEntity)).thenReturn(createdUserEntity);
    when(userConverter.toDto(createdUserEntity)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.createUser(newUserDto);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void createUserTest_should_ThrowError_WhenLoginExists() {
    when(userRepository.isExists(LOGIN)).thenReturn(true);

    assertThrows(EntityDuplicateException.class, () -> userService.createUser(newUserDto));
  }

  @Test
  public void findUserByIdTest_should_FindUser() {
    when(userRepository.findUserById(USER_ID)).thenReturn(createdUserEntity);
    when(userConverter.toDto(createdUserEntity)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.findUserById(USER_ID);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findUserByIdTest_should_ThrowError_WhenUserNotExist() {
    when(userRepository.findUserById(UN_EXISTING_ID)).thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class, () -> userService.findUserById(UN_EXISTING_ID));
  }

  @Test
  public void findUserByLoginTest_should_FindUser() {
    when(userRepository.findUserByLogin(LOGIN)).thenReturn(createdUserEntity);
    when(userConverter.toDto(createdUserEntity)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.findUserByLogin(LOGIN);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void findUserByLoginTest_should_ThrowError_WhenUserNotExist() {
    when(userRepository.findUserByLogin(UN_EXISTING_LOGIN))
        .thenThrow(EntityNotFoundException.class);

    assertThrows(EntityNotFoundException.class,
        () -> userService.findUserByLogin(UN_EXISTING_LOGIN));
  }

  @Test
  public void updateUserTest_should_UpdateUser() {
    when(userRepository.findUserById(USER_ID)).thenReturn(createdUserEntity);
    when(accountConverter.toEntity(accountDto)).thenReturn(accountEntity);
    when(userConverter.toDto(updatedUserEntity)).thenReturn(updatedUserDto);

    UserDto expectedResult = updatedUserDto;
    createdUserDto.setAccountList(List.of(accountDto));
    UserDto actualResult = userService.updateUser(createdUserDto);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void updateUserTest_should_ThrowError_WhenUserNotFound() {
    UserDto createdUserDto = getCreatedUserDto();

    createdUserDto.setId(UN_EXISTING_ID);

    assertThrows(EntityNotFoundException.class, () -> userService.updateUser(createdUserDto));
  }
}
