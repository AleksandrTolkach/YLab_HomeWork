package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.mapper.RowMapper;
import by.toukach.walletservice.entity.mapper.impl.AccountMapper;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.AccountRepository;
import by.toukach.walletservice.repository.DbInitializer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс для выполнения запросов, связанных со счетом, в память.
 */
public class AccountRepositoryImpl implements AccountRepository {

  private static final AccountRepository instance = new AccountRepositoryImpl();
  private static final String ID = "id";
  private static final String USER_ID = "user_id";

  private final DbInitializer dbInitializer;
  private final RowMapper<Account> accountRowMapper;

  private AccountRepositoryImpl() {
    dbInitializer = DbInitializerImpl.getInstance();
    accountRowMapper = AccountMapper.getInstance();
  }

  @Override
  public Account createAccount(Account account) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO application.accounts (created_at, title, sum, user_id) "
            + "VALUES (?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS)) {

      statement.setObject(1, account.getCreatedAt());
      statement.setString(2, account.getTitle());
      statement.setBigDecimal(3, account.getSum());
      statement.setLong(4, account.getUserId());

      statement.execute();

      ResultSet generatedKeys = statement.getGeneratedKeys();

      if (generatedKeys.next()) {
        account.setId(generatedKeys.getLong(ID));
      }

    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.SAVE_ACCOUNT, e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(ExceptionMessage.CLOSE_CONNECTION_TO_DB);
      }
    }

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
    Connection connection = dbInitializer.getConnection();
    Long id = account.getId();

    try (PreparedStatement statement = connection.prepareStatement(
            "UPDATE application.accounts SET title = ?, sum = ? "
                + "WHERE id = ?")) {

      statement.setString(1, account.getTitle());
      statement.setBigDecimal(2, account.getSum());
      statement.setLong(3, id);

      int updatedRows = statement.executeUpdate();

      return updatedRows != 0 ? findAccountById(id) : Optional.empty();

    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.ACCOUNT_UPDATE, e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(ExceptionMessage.CLOSE_CONNECTION_TO_DB);
      }
    }
  }

  public static AccountRepository getInstance() {
    return instance;
  }


  private List<Account> findAccountBy(String argumentName, Object argumentValue) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(
            "SELECT id AS account_id, created_at AS account_created_at, title AS account_title, "
                + "sum AS account_sum, user_id "
                + "FROM application.accounts "
                + "WHERE " + argumentName + " = ? ")) {

      statement.setObject(1, argumentValue);

      ResultSet resultSet = statement.executeQuery();

      List<Account> accountList = new ArrayList<>();
      while (resultSet.next()) {
        accountList.add(accountRowMapper.mapRow(resultSet));
      }

      return accountList;

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
