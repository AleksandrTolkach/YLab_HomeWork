package by.toukach.walletservice.infrastructure.repositories.impl;

import by.toukach.walletservice.infrastructure.entity.UserEntity;
import by.toukach.walletservice.infrastructure.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для выполнения запросов, связанных с пользователями, в память.
 * */
public class UserRepositoryImpl implements UserRepository {

  public static final UserRepository instance = new UserRepositoryImpl();
  private static final String ADMIN_LOGIN = "admin";
  private static final String ADMIN_PASSWORD = "admin";

  private final List<UserEntity> userList = new ArrayList<>();
  private Long sequence = 0L;

  private UserRepositoryImpl() {
    UserEntity admin = UserEntity.builder()
        .id(++sequence)
        .login(ADMIN_LOGIN)
        .password(ADMIN_PASSWORD)
        .accountList(new ArrayList<>())
        .build();

    userList.add(admin);
  }

  @Override
  public UserEntity createUser(UserEntity user) {
    user.setId(++sequence);
    userList.add(user);
    return user;
  }

  @Override
  public UserEntity findUserById(Long id) {
    return userList.stream()
        .filter(u -> u.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Override
  public UserEntity findUserByLogin(String login) {
    return userList.stream().filter(u -> u.getLogin().equals(login))
        .findFirst()
        .orElse(null);
  }

  @Override
  public boolean isExists(Long id) {
    return findUserById(id) != null;
  }

  @Override
  public boolean isExists(String login) {
    return findUserByLogin(login) != null;
  }

  public static UserRepository getInstance() {
    return instance;
  }
}
