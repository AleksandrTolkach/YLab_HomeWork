package by.toukach.walletservice.domain.services.impl;

import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.UserService;
import by.toukach.walletservice.exceptions.EntityDuplicateException;
import by.toukach.walletservice.exceptions.EntityNotFoundException;
import by.toukach.walletservice.exceptions.ExceptionMessage;
import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.entity.UserEntity;
import by.toukach.walletservice.infrastructure.entity.converter.Converter;
import by.toukach.walletservice.infrastructure.entity.converter.impl.AccountConverter;
import by.toukach.walletservice.infrastructure.entity.converter.impl.UserConverter;
import by.toukach.walletservice.infrastructure.repositories.UserRepository;
import by.toukach.walletservice.infrastructure.repositories.impl.UserRepositoryImpl;
import java.util.List;

/**
 * Класс для выполнения операция с пользователями.
 * */
public class UserServiceImpl implements UserService {

  private static final UserService instance = new UserServiceImpl();

  private final UserRepository userRepository;
  private final Converter<UserEntity, UserDto> userConverter;
  private final Converter<AccountEntity, AccountDto> accountConverter;

  private UserServiceImpl() {
    userRepository = UserRepositoryImpl.getInstance();
    userConverter = UserConverter.getInstance();
    accountConverter = AccountConverter.getInstance();
  }

  @Override
  public UserDto createUser(UserDto user) {
    if (userRepository.isExists(user.getLogin())) {
      throw new EntityDuplicateException(String.format(ExceptionMessage.USER_DUPLICATE,
          user.getLogin()));
    }
    UserEntity userEntity = userRepository.createUser(userConverter.toEntity(user));
    return userConverter.toDto(userEntity);
  }

  @Override
  public UserDto findUserById(Long id) {
    UserEntity user = userRepository.findUserById(id);
    if (user == null) {
      throw new EntityNotFoundException(String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, id));
    }
    return userConverter.toDto(user);
  }

  @Override
  public UserDto findUserByLogin(String login) {
    UserEntity user = userRepository.findUserByLogin(login);
    if (user == null) {
      throw new EntityNotFoundException(
          String.format(ExceptionMessage.USER_BY_LOGIN_NOT_FOUND, login));
    }
    return userConverter.toDto(user);
  }

  @Override
  public UserDto updateUser(UserDto user) {
    Long id = user.getId();
    UserEntity userEntity = userRepository.findUserById(id);
    if (userEntity == null) {
      throw new EntityNotFoundException(String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, id));
    }

    List<AccountDto> accountDtoList = user.getAccountList();
    List<AccountEntity> accountEntityList = accountDtoList.stream()
        .map(accountConverter::toEntity)
        .toList();

    userEntity.setAccountList(accountEntityList);

    return userConverter.toDto(userEntity);
  }

  @Override
  public boolean isExists(Long id) {
    return userRepository.isExists(id);
  }

  public static UserService getInstance() {
    return instance;
  }
}
