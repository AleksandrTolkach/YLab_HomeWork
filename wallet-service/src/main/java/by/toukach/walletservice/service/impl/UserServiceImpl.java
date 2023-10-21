package by.toukach.walletservice.service.impl;

import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.entity.converter.Converter;
import by.toukach.walletservice.entity.converter.impl.UserConverter;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.UserRepository;
import by.toukach.walletservice.repository.impl.UserRepositoryImpl;
import by.toukach.walletservice.service.UserService;
import java.time.LocalDateTime;

/**
 * Класс для выполнения операция с пользователями.
 */
public class UserServiceImpl implements UserService {

  private static final UserService instance = new UserServiceImpl();

  private final UserRepository userRepository;
  private final Converter<User, UserDto> userConverter;

  private UserServiceImpl() {
    userRepository = UserRepositoryImpl.getInstance();
    userConverter = UserConverter.getInstance();
  }

  @Override
  public UserDto createUser(UserDto userDto) {
    if (userRepository.isExists(userDto.getLogin())) {
      throw new EntityDuplicateException(String.format(ExceptionMessage.USER_DUPLICATE,
          userDto.getLogin()));
    } else {
      userDto.setCreatedAt(LocalDateTime.now());

      User user = userRepository.createUser(userConverter.toEntity(userDto));
      return userConverter.toDto(user);
    }
  }

  @Override
  public UserDto findUserById(Long id) {
    User user = userRepository.findUserById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, id)));

    return userConverter.toDto(user);
  }

  @Override
  public UserDto findUserByLogin(String login) {
    User user = userRepository.findUserByLogin(login).orElseThrow(() ->
        new EntityNotFoundException(String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, login)));

    return userConverter.toDto(user);
  }

  @Override
  public boolean isExists(Long id) {
    return userRepository.isExists(id);
  }

  public static UserService getInstance() {
    return instance;
  }
}
