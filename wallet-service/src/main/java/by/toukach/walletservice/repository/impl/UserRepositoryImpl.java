package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для выполнения запросов, связанных с пользователями, в память.
 * */
public class UserRepositoryImpl implements UserRepository {

  public static final UserRepository instance = new UserRepositoryImpl();
  private static final String ADMIN_LOGIN = "admin";
  private static final String ADMIN_PASSWORD = "admin";

  private final List<User> userList = new ArrayList<>();
  private Long sequence = 0L;

  private UserRepositoryImpl() {
    User admin = User.builder()
        .id(++sequence)
        .login(ADMIN_LOGIN)
        .password(ADMIN_PASSWORD)
        .accountList(new ArrayList<>())
        .build();

    userList.add(admin);
  }

  @Override
  public User createUser(User user) {
    user.setId(++sequence);
    userList.add(user);
    return user;
  }

  @Override
  public User findUserById(Long id) {
    return userList.stream()
        .filter(u -> u.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Override
  public User findUserByLogin(String login) {
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
