package by.toukach.walletservice.utils;

import by.toukach.walletservice.exception.ExceptionMessage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Утилитарный класс для подготовки базы данных перед использованием.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DbInitializer {

  private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5434/toukach";
  private static final String WALLET_URL = "jdbc:postgresql://localhost:5434/wallet";
  private static final String USERNAME = "toukach";
  private static final String PASSWORD = "ylab";
  private static final String CREATE_WALLET_DB_SQL = "CREATE DATABASE wallet";
  private static final String CREATE_SCHEMA_APPLICATION_SQL =
      "CREATE SCHEMA application AUTHORIZATION toukach";
  private static final String CREATE_SCHEMA_LIQUIBASE_SQL =
      "CREATE SCHEMA liquibase AUTHORIZATION toukach";

  /**
   * Метод, который выполняет создание БД и схем для приложения, если они не существуют.
   */
  public static void prepareDb() {
    try (Connection connection =
        DriverManager.getConnection(DEFAULT_URL, USERNAME, PASSWORD);
        Statement statement = connection.createStatement()) {

      statement.executeUpdate(CREATE_WALLET_DB_SQL);

    } catch (SQLException e) {
      System.err.println(ExceptionMessage.DB_EXISTS);
    }

    try (Connection connection =
        DriverManager.getConnection(WALLET_URL, USERNAME, PASSWORD);
        Statement statement = connection.createStatement()) {
      statement.executeUpdate(CREATE_SCHEMA_APPLICATION_SQL);
      statement.executeUpdate(CREATE_SCHEMA_LIQUIBASE_SQL);
    } catch (SQLException e) {
      System.err.println(ExceptionMessage.SCHEMES_EXISTS);
    }
  }
}
