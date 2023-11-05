package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.repository.TransactionRepository;
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
 * Класс для выполнения запросов, связанных с операциями, в память.
 */
@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

  private static final String ID = "id";
  private static final String USER_ID = "user_id";
  private static final String INSERT_SQL =
      "INSERT INTO application.transactions (created_at, type, user_id, account_id, value) "
          + "VALUES (?, ?, ?, ?, ?) RETURNING ID";
  private static final String EXISTS_SQL =
      "SELECT EXISTS (SELECT 1 FROM application.transactions WHERE id = ?)";
  private static final String SELECT_SQL =
      "SELECT id, created_at, type, user_id, account_id, value "
          + "FROM application.transactions "
          + "WHERE %s = '%s'";

  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<Transaction> transactionRowMapper;

  @Override
  public Transaction createTransaction(Transaction transaction) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement statement = connection.prepareStatement(INSERT_SQL,
          Statement.RETURN_GENERATED_KEYS);

      statement.setObject(1, transaction.getCreatedAt());
      statement.setString(2, transaction.getType().name());
      statement.setDouble(3, transaction.getUserId());
      statement.setDouble(4, transaction.getAccountId());
      statement.setBigDecimal(5, transaction.getValue());
      return statement;
    }, keyHolder);

    Long id = keyHolder.getKey() != null ? keyHolder.getKeyAs(Long.class) : null;
    transaction.setId(id);

    return transaction;
  }

  @Override
  public Optional<Transaction> findTransactionById(Long id) {
    List<Transaction> transactionList = findTransactionsBy(ID, id);

    return !transactionList.isEmpty() ? Optional.of(transactionList.get(0)) : Optional.empty();
  }

  @Override
  public List<Transaction> findTransactionByUserId(Long userId) {
    return findTransactionsBy(USER_ID, userId);
  }

  @Override
  public boolean isExists(Long id) {
    return Boolean.TRUE.equals(
        jdbcTemplate.queryForObject(EXISTS_SQL, Boolean.class, id));
  }

  private List<Transaction> findTransactionsBy(String argumentName, Object argumentValue) {
    return jdbcTemplate.query(String.format(SELECT_SQL, argumentName, argumentValue),
        transactionRowMapper);
  }
}
