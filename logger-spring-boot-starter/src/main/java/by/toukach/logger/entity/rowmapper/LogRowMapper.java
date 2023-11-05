package by.toukach.logger.entity.rowmapper;

import by.toukach.logger.entity.Log;
import by.toukach.logger.enumiration.LogType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.RowMapper;

/**
 * Класс для создания Log из ResultSet.
 * */
public class LogRowMapper implements RowMapper<Log> {

  private static final String ID = "id";
  private static final String TYPE = "type";
  private static final String VALUE = "value";
  private static final String CREATED_AT = "created_at";

  @Override
  public Log mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return Log.builder()
        .id(resultSet.getLong(ID))
        .type(LogType.valueOf(resultSet.getString(TYPE)))
        .message(resultSet.getString(VALUE))
        .createdAt(resultSet.getObject(CREATED_AT, LocalDateTime.class))
        .build();
  }
}
