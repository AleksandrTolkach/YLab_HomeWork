package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.DbInitializer;
import by.toukach.walletservice.repository.Migration;
import by.toukach.walletservice.utils.param.ConfigParamProvider;
import by.toukach.walletservice.utils.param.ConfigParamProvider.ConfigParamVar;
import java.sql.Connection;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CommandExecutionException;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * Класс для работы с инструментами миграции БД.
 */
public class MigrationImpl implements Migration {

  private static final Migration instance = new MigrationImpl();
  private final DbInitializer dbInitializer;
  private static final String CHANGE_LOG_FILE_PATH =
      ConfigParamProvider.getParam(ConfigParamVar.DB_CHANGE_LOG_FILE);

  private MigrationImpl() {
    dbInitializer = DbInitializerImpl.getInstance();
  }

  @Override
  public void migrate() {
    Connection connection = dbInitializer.getConnection();

    try {
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      database.setLiquibaseSchemaName(
          ConfigParamProvider.getParam(ConfigParamVar.DB_LIQUIBASE_SCHEMA));

      CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
      updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database);
      updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, CHANGE_LOG_FILE_PATH);
      updateCommand.execute();
    } catch (DatabaseException | CommandExecutionException e) {
      throw new DbException(ExceptionMessage.MIGRATION, e);
    }

    try {
      connection.close();
    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.CLOSE_CONNECTION_TO_DB, e);
    }
  }

  @Override
  public void rollback(String tag) {
    Connection connection = dbInitializer.getConnection();

    try {
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      database.setLiquibaseSchemaName(
          ConfigParamProvider.getParam(ConfigParamVar.DB_LIQUIBASE_SCHEMA));

      Liquibase liquibase = new Liquibase(CHANGE_LOG_FILE_PATH, new ClassLoaderResourceAccessor(),
          database);

      liquibase.rollback(tag, null, new Contexts(),
          new LabelExpression());
    } catch (LiquibaseException e) {
      throw new DbException(ExceptionMessage.MIGRATION, e);
    }

    try {
      connection.close();
    } catch (SQLException e) {
      throw new DbException(ExceptionMessage.CLOSE_CONNECTION_TO_DB, e);
    }
  }

  public static Migration getInstance() {
    return instance;
  }
}
