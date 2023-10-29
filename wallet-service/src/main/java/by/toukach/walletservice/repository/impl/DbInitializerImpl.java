package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.config.PropertyConfig;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.DbInitializer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Класс для настройки соединений к базе и предоставляющий Connection.
 */
@Component
@RequiredArgsConstructor
public class DbInitializerImpl implements DbInitializer {

  private final PropertyConfig propertyConfig;

  @Override
  public Connection getConnection() {
    try {
      return DriverManager.getConnection(propertyConfig.getDataBaseUrl()
              + propertyConfig.getDataBaseName(), propertyConfig.getDataBaseUsername(),
          propertyConfig.getDataBasePassword());
    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.CONNECT_TO_DB, e);
    }
  }

  @Override
  public void prepareDb() {
    String dataBaseDriver = propertyConfig.getDataBaseDriver();

    try {
      Class.forName(dataBaseDriver);
    } catch (ClassNotFoundException e) {
      throw new DbException(ExceptionMessage.DB_DRIVER_DID_NOT_LOAD, e);
    }

    String url = propertyConfig.getDataBaseUrl();
    String dataBaseName = propertyConfig.getDataBaseName();
    String username = propertyConfig.getDataBaseUsername();
    String password = propertyConfig.getDataBasePassword();

    try (Connection connection =
        DriverManager.getConnection(url + username, username, password);
        Statement statement = connection.createStatement()) {

      statement.executeUpdate("CREATE DATABASE wallet");

    } catch (SQLException e) {
      System.err.println(ExceptionMessage.DB_EXISTS);
    }

    try (Connection connection =
        DriverManager.getConnection(url + dataBaseName, username, password);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate("CREATE SCHEMA application AUTHORIZATION toukach");
      statement.executeUpdate("CREATE SCHEMA liquibase AUTHORIZATION toukach");
    } catch (SQLException e) {
      System.err.println(ExceptionMessage.SCHEMES_EXISTS);
    }
  }
}
