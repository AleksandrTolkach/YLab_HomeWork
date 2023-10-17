package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.DbInitializer;
import by.toukach.walletservice.utils.param.ConfigParamProvider;
import by.toukach.walletservice.utils.param.ConfigParamProvider.ConfigParamVar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для настройки соединений к базе и предоставляющий Connection.
 */
public class DbInitializerImpl implements DbInitializer {

  private static DbInitializer instance = new DbInitializerImpl();

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
    String username = ConfigParamProvider.getParam(ConfigParamVar.DB_USERNAME);
    String password = ConfigParamProvider.getParam(ConfigParamVar.DB_PASSWORD);

    try {
      return DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.CONNECT_TO_DB, e);
    }
  }

  public static DbInitializer getInstance() {
    return instance;
  }
}
