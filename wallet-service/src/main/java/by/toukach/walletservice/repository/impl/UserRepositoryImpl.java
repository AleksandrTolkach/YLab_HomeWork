package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.repository.UserRepository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Класс для выполнения запросов, связанных с пользователями, в память.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private static final String ID = "id";
  private static final String LOGIN = "login";
  private static final String INSERT_USER_SQL =
      "INSERT INTO application.users (created_at, login, password, role) "
      + "VALUES (?, ?, ?, ?) RETURNING ID";
  private static final String SELECT_USER_SQL =
      "SELECT users.id, users.created_at, login, password, role, "
          + "accounts.id AS account_id, accounts.created_at AS account_created_at, "
          + "title AS account_title, sum AS account_sum, user_id "
          + "FROM application.users AS users "
          + "LEFT JOIN application.accounts AS accounts ON users.id = accounts.user_id "
          + "WHERE users.%s = '%s' ";
  private static final String EXISTS_SQL = "SELECT EXISTS (SELECT 1 FROM application.users "
      + "WHERE %s = '%s')";

  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<User> userRowMapper;

  @Override
  public User createUser(User user) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL,
          Statement.RETURN_GENERATED_KEYS);

      statement.setObject(1, user.getCreatedAt());
      statement.setObject(2, user.getLogin());
      statement.setObject(3, user.getPassword());
      statement.setObject(4, user.getRole().name());
      return statement;
    }, keyHolder);

    Long id = keyHolder.getKey() != null ? keyHolder.getKeyAs(Long.class) : null;
    user.setId(id);

    return user;
  }

  @Override
  public Optional<User> findUserById(Long id) {
    return findUserBy(ID, id);
  }

  @Override
  public Optional<User> findUserByLogin(String login) {
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

  private Optional<User> findUserBy(String argumentName, Object argumentValue) {
    User user;

    try {
      user = jdbcTemplate.queryForObject(String.format(SELECT_USER_SQL, argumentName,
          argumentValue), userRowMapper::mapRow);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }

    return Optional.ofNullable(user);
  }

  private boolean isExistsBy(String argumentName, Object argumentValue) {
    return Boolean.TRUE.equals(
        jdbcTemplate.queryForObject(String.format(EXISTS_SQL, argumentName, argumentValue),
            Boolean.class));
  }
}
