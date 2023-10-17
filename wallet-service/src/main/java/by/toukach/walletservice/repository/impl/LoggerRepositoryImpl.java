package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.mapper.RowMapper;
import by.toukach.walletservice.entity.mapper.impl.LogMapper;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.DbInitializer;
import by.toukach.walletservice.repository.LoggerRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для выполнения запросов, связанных с логами, в память.
 */
public class LoggerRepositoryImpl implements LoggerRepository {

  private static final LoggerRepository instance = new LoggerRepositoryImpl();
  private static final String ID = "id";

  private final DbInitializer dbInitializer;
  private final RowMapper<Log> logRowMapper;

  private LoggerRepositoryImpl() {
    dbInitializer = DbInitializerImpl.getInstance();
    logRowMapper = LogMapper.getInstance();
  }

  @Override
  public Log createLog(Log log) {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(
        "INSERT INTO application.logs (type, value , created_at) "
            + "VALUES (?, ?, ?) RETURNING ID")) {

      statement.setObject(1, log.getType().name());
      statement.setObject(2, log.getValue());
      statement.setObject(3, log.getCreatedAt());

      statement.execute();

      ResultSet generatedKeys = statement.getResultSet();

      if (generatedKeys.next()) {
        log.setId(generatedKeys.getLong(ID));
      }

    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.SAVE_LOG, e);
    } finally {
      try {
        connection.close();
      } catch (SQLException e) {
        System.err.println(ExceptionMessage.CLOSE_CONNECTION_TO_DB);
      }
    }

    return log;
  }

  @Override
  public List<Log> findLogs() {
    Connection connection = dbInitializer.getConnection();

    try (PreparedStatement statement = connection.prepareStatement(
        "SELECT id, type, value , created_at FROM application.logs")) {

      statement.executeQuery();

      ResultSet resultSet = statement.getResultSet();

      List<Log> logList = new ArrayList<>();
      while (resultSet.next()) {
        logList.add(logRowMapper.mapRow(resultSet));
      }

      return logList;

    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.DB_REQUEST, e);
    }
  }

  public static LoggerRepository getInstance() {
    return instance;
  }
}
