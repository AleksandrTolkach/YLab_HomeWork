package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.entity.mapper.RowMapper;
import by.toukach.walletservice.entity.mapper.impl.UserMapper;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.DbInitializer;
import by.toukach.walletservice.repository.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс для выполнения запросов, связанных с пользователями, в память.
 * */
public class UserRepositoryImpl implements UserRepository {

  public static final UserRepository instance = new UserRepositoryImpl();
  private static final String ID = "id";
  private static final String LOGIN = "login";

  private final DbInitializer dbInitializer;
  private final RowMapper<User> userRowMapper;

  private UserRepositoryImpl() {
    dbInitializer = DbInitializerImpl.getInstance();
    userRowMapper = UserMapper.getInstance();
  }

  @Override
  public User createUser(User user) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO application.users (created_at, login, password) "
            + "VALUES (?, ?, ?) RETURNING ID")) {

      statement.setObject(1, user.getCreatedAt());
      statement.setObject(2, user.getLogin());
      statement.setObject(3, user.getPassword());

      statement.execute();

      ResultSet generatedKeys = statement.getResultSet();

      if (generatedKeys.next()) {
        user.setId(generatedKeys.getLong(ID));
      }

    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.SAVE_USER, e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(ExceptionMessage.CLOSE_CONNECTION_TO_DB);
      }
    }

    return user;
  }

  @Override
  public User findUserById(Long id) {
    return findUserBy(ID, id);
  }

  @Override
  public User findUserByLogin(String login) {
    return findUserBy(LOGIN, login);
  }

  @Override
  public boolean isExists(Long id) {
    return isExistsBy(ID, id);
  }

  @Override
  public boolean isExists(String login) {
    return isExistsBy(LOGIN, login);
  }

  public static UserRepository getInstance() {
    return instance;
  }

  private User findUserBy(String argumentName, Object argumentValue) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(
            "SELECT users.id, users.created_at, login, password, "
                + "accounts.id AS account_id, accounts.created_at AS account_created_at, "
                + "title AS account_title, sum AS account_sum, user_id "
                + "FROM application.users AS users "
                + "LEFT JOIN application.accounts AS accounts ON users.id = accounts.user_id "
                + "WHERE users." + argumentName + " = ? ")) {

      statement.setObject(1, argumentValue);

      ResultSet resultSet = statement.executeQuery();

      if (!resultSet.wasNull() && resultSet.next()) {
        return userRowMapper.mapRow(resultSet);
      } else {
        throw new EntityNotFoundException(String.format(ExceptionMessage.USER_NOT_FOUND,
            argumentName, argumentValue));
      }

    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.DB_REQUEST, e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(ExceptionMessage.CLOSE_CONNECTION_TO_DB);
      }
    }
  }

  private boolean isExistsBy(String argumentName, Object argumentValue) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement =
        connection.prepareStatement("SELECT EXISTS (SELECT 1 FROM application.users "
            + "WHERE "  + argumentName + " = ?)")) {

      statement.setObject(1, argumentValue);

      ResultSet resultSet = statement.executeQuery();

      if (!resultSet.wasNull() && resultSet.next()) {
        return resultSet.getBoolean(1);
      } else {
        throw new DbException(ExceptionMessage.DB_REQUEST);
      }

    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.DB_REQUEST, e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(ExceptionMessage.CLOSE_CONNECTION_TO_DB);
      }
    }
  }
}
