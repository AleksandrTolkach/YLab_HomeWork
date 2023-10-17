package by.toukach.walletservice.entity.mapper.impl;

import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.mapper.RowMapper;
import by.toukach.walletservice.enumiration.LogType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для создания Log из ResultSet.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogMapper implements RowMapper<Log> {

  private static final RowMapper<Log> instance = new LogMapper();
  private static final String ID = "id";
  private static final String TYPE = "type";
  private static final String VALUE = "value";
  private static final String CREATED_AT = "created_at";

  @Override
  public Log mapRow(ResultSet resultSet) throws SQLException {
    return Log.builder()
        .id(resultSet.getLong(ID))
        .type(LogType.valueOf(resultSet.getString(TYPE)))
        .value(resultSet.getString(VALUE))
        .createdAt(resultSet.getObject(CREATED_AT, LocalDateTime.class))
        .build();
  }

  public static RowMapper<Log> getInstance() {
    return instance;
  }
}
