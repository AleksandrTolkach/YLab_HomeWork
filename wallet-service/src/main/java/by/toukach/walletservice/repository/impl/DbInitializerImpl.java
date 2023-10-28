package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.DbInitializer;
import by.toukach.walletservice.utils.param.ConfigParamProvider;
import by.toukach.walletservice.utils.param.ConfigParamProvider.ConfigParamVar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.stereotype.Component;

/**
 * Класс для настройки соединений к базе и предоставляющий Connection.
 */
@Component
public class DbInitializerImpl implements DbInitializer {

  private String dbDriver;

  private DbInitializerImpl() {
    try {
      Class.forName(ConfigParamProvider.getParam(ConfigParamVar.DB_DRIVER));
    } catch (ClassNotFoundException e) {
      throw new DbException(ExceptionMessage.DB_DRIVER_DID_NOT_LOAD, e);
    }
  }

  @Override
  public Connection getConnection() {
    String url = ConfigParamProvider.getParam(ConfigParamVar.DB_URL);
    String dbName = ConfigParamProvider.getParam(ConfigParamVar.DB_NAME);
    String username = ConfigParamProvider.getParam(ConfigParamVar.DB_USERNAME);
    String password = ConfigParamProvider.getParam(ConfigParamVar.DB_PASSWORD);

    try {
      return DriverManager.getConnection(String.format(url, dbName), username, password);
    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.CONNECT_TO_DB, e);
    }
  }

  @Override
  public void prepareDb() {
    String url = ConfigParamProvider.getParam(ConfigParamVar.DB_URL);
    String dbName = ConfigParamProvider.getParam(ConfigParamVar.DB_NAME);
    String username = ConfigParamProvider.getParam(ConfigParamVar.DB_USERNAME);
    String password = ConfigParamProvider.getParam(ConfigParamVar.DB_PASSWORD);

    try (Connection connection =
        DriverManager.getConnection(String.format(url, username), username, password);
        Statement statement = connection.createStatement()) {

      statement.executeUpdate("CREATE DATABASE wallet");

    } catch (SQLException e) {
      System.err.println(ExceptionMessage.DB_EXISTS);
    }

    try (Connection connection =
        DriverManager.getConnection(String.format(url, dbName), username, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate("CREATE SCHEMA application AUTHORIZATION toukach");
      statement.executeUpdate("CREATE SCHEMA liquibase AUTHORIZATION toukach");
    } catch (SQLException e) {
      System.err.println(ExceptionMessage.SCHEMES_EXISTS);
    }
  }
}
