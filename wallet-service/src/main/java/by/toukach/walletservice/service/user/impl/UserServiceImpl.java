package by.toukach.walletservice.service.user.impl;

import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.exception.EntityDuplicateException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.mapper.UserMapper;
import by.toukach.walletservice.repository.UserRepository;
import by.toukach.walletservice.service.user.UserService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Класс для выполнения операция с пользователями.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDto createUser(UserDto userDto) {
    if (userRepository.isExists(userDto.getLogin())) {
      throw new EntityDuplicateException(String.format(ExceptionMessage.USER_DUPLICATE,
          userDto.getLogin()));
    } else {
      userDto.setCreatedAt(LocalDateTime.now());
      userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

      User user = userRepository.createUser(userMapper.userDtoToUser(userDto));
      return userMapper.userToUserDto(user);
    }
  }

  @Override
  public UserDto findUserById(Long id) {
    User user = userRepository.findUserById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format(ExceptionMessage.USER_BY_ID_NOT_FOUND, id)));

    return userMapper.userToUserDto(user);
  }

  @Override
  public UserDto findUserByLogin(String login) {
    User user = userRepository.findUserByLogin(login).orElseThrow(
        () -> new EntityNotFoundException(String.format(ExceptionMessage.USER_BY_LOGIN_NOT_FOUND,
            login)));

    return userMapper.userToUserDto(user);
  }

  @Override
  public boolean isExists(Long id) {
    return userRepository.isExists(id);
  }
}
