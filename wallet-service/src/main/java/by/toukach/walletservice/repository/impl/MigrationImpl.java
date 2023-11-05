package by.toukach.walletservice.repository.impl;

import by.toukach.walletservice.config.properties.DbProperties;
import by.toukach.walletservice.config.properties.LiquibaseProperties;
import by.toukach.walletservice.exception.DbException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.Migration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Класс для работы с инструментами миграции БД.
 */
@Repository
@RequiredArgsConstructor
public class MigrationImpl implements Migration {

  private final DbProperties dbProperties;
  private final LiquibaseProperties liquibaseProperties;

  @Override
  public void rollback(String tag) {
    try (Connection connection = DriverManager.getConnection(dbProperties.getUrl(),
        dbProperties.getUsername(), dbProperties.getPassword())) {
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      database.setLiquibaseSchemaName(liquibaseProperties.getLiquibaseSchema());

      Liquibase liquibase = new Liquibase(liquibaseProperties.getChangeLog(),
          new ClassLoaderResourceAccessor(), database);

      liquibase.rollback(tag, null, new Contexts(),
          new LabelExpression());
    } catch (SQLException | LiquibaseException e) {
      throw new DbException(ExceptionMessage.MIGRATION, e);
    }
  }
}
