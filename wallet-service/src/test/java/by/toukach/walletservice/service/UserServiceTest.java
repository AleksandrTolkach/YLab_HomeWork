package by.toukach.walletservice.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import by.toukach.walletservice.BaseTest;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.entity.mapper.UserMapper;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.repository.UserRepository;
import by.toukach.walletservice.service.impl.UserServiceImpl;
import java.lang.reflect.InvocationTargetException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest extends BaseTest {

  @InjectMocks
  private UserServiceImpl userService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private PasswordEncoder passwordEncoder;
  private UserDto newUserDto;
  private UserDto createdUserDto;
  private User createdUser;
  private UserDto newUserDtoWithRole;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException,
      IllegalArgumentException, InstantiationException, IllegalAccessException {
    newUserDto = getNewUserDto();
    createdUserDto = getCreatedUserDto();
    createdUser = getCreatedUser();
    newUserDtoWithRole = getNewUserDtoWithRole();
  }

  @Test
  @DisplayName("Тест создания пользователя в приложении")
  public void createUserTest_should_CreateUser() {
    when(userRepository.isExists(LOGIN)).thenReturn(false);
    when(userMapper.userDtoToUser(newUserDtoWithRole)).thenReturn(createdUser);
    when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
    when(userRepository.createUser(any())).thenReturn(createdUser);
    when(userMapper.userToUserDto(createdUser)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.createUser(newUserDtoWithRole);

    assertThat(actualResult).isEqualTo(expectedResult);
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
    when(userRepository.findUserById(USER_ID)).thenReturn(Optional.of(createdUser));
    when(userMapper.userToUserDto(createdUser)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.findUserById(USER_ID);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя в приложении по несуществующему ID")
  public void findUserByIdTest_should_ThrowError_WhenUserNotExist() {
    when(userRepository.findUserById(UN_EXISTING_ID)).thenReturn(Optional.empty());
    when(userMapper.userToUserDto(createdUser)).thenReturn(createdUserDto);

    assertThatThrownBy(() -> userService.findUserById(UN_EXISTING_ID))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("Тест поиска пользователя в приложении по логину")
  public void findUserByLoginTest_should_FindUser() {
    when(userRepository.findUserByLogin(LOGIN)).thenReturn(Optional.of(createdUser));
    when(userMapper.userToUserDto(createdUser)).thenReturn(createdUserDto);

    UserDto expectedResult = createdUserDto;
    UserDto actualResult = userService.findUserByLogin(LOGIN);

    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Тест поиска пользователя в приложении по несуществующему логину")
  public void findUserByLoginTest_should_ThrowError_WhenUserNotExist() {
    when(userRepository.findUserByLogin(UN_EXISTING_LOGIN))
        .thenThrow(EntityNotFoundException.class);

    assertThatThrownBy(() -> userService.findUserByLogin(UN_EXISTING_LOGIN))
        .isInstanceOf(EntityNotFoundException.class);
  }
}
