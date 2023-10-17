package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.mapper.RowMapper;
import by.toukach.walletservice.entity.mapper.impl.TransactionMapper;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.DbInitializer;
import by.toukach.walletservice.repository.TransactionRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для выполнения запросов, связанных с операциями, в память.
 * */
public class TransactionRepositoryImpl implements TransactionRepository {

  private static final TransactionRepository instance = new TransactionRepositoryImpl();
  private static final String ID = "id";
  private static final String USER_ID = "user_id";

  private final DbInitializer dbInitializer;
  private final RowMapper<Transaction> transactionRowMapper;

  private TransactionRepositoryImpl() {
    dbInitializer = DbInitializerImpl.getInstance();
    transactionRowMapper = TransactionMapper.getInstance();
  }

  @Override
  public Transaction createTransaction(Transaction transaction) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement =
        connection.prepareStatement(
            "INSERT INTO application.transactions (created_at, type, user_id, "
                + "account_id, value) "
                + "VALUES (?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS)) {

      statement.setObject(1, transaction.getCreatedAt());
      statement.setString(2, transaction.getType().name());
      statement.setDouble(3, transaction.getUserId());
      statement.setDouble(4, transaction.getAccountId());
      statement.setDouble(5, transaction.getValue());

      statement.execute();

      ResultSet generatedKeys = statement.getGeneratedKeys();

      if (generatedKeys.next()) {
        transaction.setId(generatedKeys.getLong(ID));
      }
    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.SAVE_TRANSACTION, e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(ExceptionMessage.CLOSE_CONNECTION_TO_DB);
      }
    }

    return transaction;
  }

  @Override
  public Transaction findTransactionById(Long id) {
    List<Transaction> transactionList = findTransactionsBy(ID, id);

    if (!transactionList.isEmpty()) {
      return transactionList.get(0);
    } else {
      throw new EntityNotFoundException(String.format(ExceptionMessage.TRANSACTION_NOT_FOUND, id));
    }
  }

  @Override
  public List<Transaction> findTransactionByUserId(Long userId) {
    return findTransactionsBy(USER_ID, userId);
  }

  @Override
  public boolean isExists(Long id) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement =
        connection.prepareStatement("SELECT EXISTS (SELECT 1 FROM application.transactions "
            + "WHERE id = ?)")) {

      statement.setLong(1, id);

      ResultSet resultSet = statement.executeQuery();

      if (resultSet != null) {
        resultSet.next();
        return resultSet.getBoolean(1);
      } else {
        return false;
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

  public static TransactionRepository getInstance() {
    return instance;
  }

  private List<Transaction> findTransactionsBy(String argumentName, Object argumentValue) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(
            "SELECT id, created_at, type, user_id, account_id, value "
                + "FROM application.transactions "
                + "WHERE " + argumentName + " = ?")) {

      statement.setObject(1, argumentValue);

      ResultSet resultSet = statement.executeQuery();

      List<Transaction> transactionList = new ArrayList<>();
      while (resultSet.next()) {
        transactionList.add(transactionRowMapper.mapRow(resultSet));
      }

      return transactionList;

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
