package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.repository.AccountRepository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Класс для выполнения запросов, связанных со счетом, в память.
 */
@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

  private static final String ID = "id";
  private static final String USER_ID = "user_id";
  private static final String INSERT_SQL =
      "INSERT INTO application.accounts (created_at, title, sum, user_id) "
          + "VALUES (?, ?, ?, ?) RETURNING ID";
  private static final String UPDATE_SQL =
      "UPDATE application.accounts SET title = ?, sum = ? WHERE id = ?";
  private static final String SELECT_SQL = "SELECT id AS account_id, "
      + "created_at AS account_created_at, title AS account_title, sum AS account_sum, user_id "
      + "FROM application.accounts "
      + "WHERE %s = '%s'";

  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<Account> accountRowMapper;

  @Override
  public Account createAccount(Account account) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement statement = connection.prepareStatement(INSERT_SQL,
          Statement.RETURN_GENERATED_KEYS);

      statement.setObject(1, account.getCreatedAt());
      statement.setString(2, account.getTitle());
      statement.setBigDecimal(3, account.getSum());
      statement.setLong(4, account.getUserId());
      return statement;
    }, keyHolder);

    Long id = keyHolder.getKey() != null ? keyHolder.getKeyAs(Long.class) : null;
    account.setId(id);

    return account;
  }

  @Override
  public Optional<Account> findAccountById(Long id) {
    List<Account> accountList = findAccountBy(ID, id);

    return !accountList.isEmpty() ? Optional.of(accountList.get(0)) : Optional.empty();
  }

  @Override
  public List<Account> findAccountsByUserId(Long userId) {
    return findAccountBy(USER_ID, userId);
  }

  @Override
  public Optional<Account> updateAccount(Account account) {
    Long id = account.getId();

    int updatedRows = jdbcTemplate.update(UPDATE_SQL, account.getTitle(), account.getSum(), id);

    return updatedRows != 0 ? findAccountById(id) : Optional.empty();
  }

  private List<Account> findAccountBy(String argumentName, Object argumentValue) {
    return jdbcTemplate.query(String.format(SELECT_SQL, argumentName, argumentValue),
        accountRowMapper::mapRow);
  }
}
