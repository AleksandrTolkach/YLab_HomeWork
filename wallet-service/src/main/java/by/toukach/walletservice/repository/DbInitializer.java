package by.toukach.walletservice.repository;

import java.sql.Connection;

/**
 * Интерфейс для настройки соединения к базе и предоставляющий Connection.
 * */
public interface DbInitializer {

  /**
   * Метод предоставляющий доступ к настроенному Connection.
   *
   * @return настроенный Connection.
   */
  Connection getConnection();

  /**
   * Метод для создания БД и схем.
   */
  void prepareDb();
}
