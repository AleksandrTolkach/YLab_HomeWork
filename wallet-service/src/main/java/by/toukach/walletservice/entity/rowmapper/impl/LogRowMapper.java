package by.toukach.walletservice.entity.rowmapper.impl;

import by.toukach.walletservice.entity.Log;
import by.toukach.walletservice.entity.rowmapper.RowMapper;
import by.toukach.walletservice.enumiration.LogType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Класс для создания Log из ResultSet.
 * */
@Component
public class LogRowMapper implements RowMapper<Log> {

  private static final String ID = "id";
  private static final String TYPE = "type";
  private static final String VALUE = "value";
  private static final String CREATED_AT = "created_at";

  @Override
  public Log mapRow(ResultSet resultSet) throws SQLException {
    return Log.builder()
        .id(resultSet.getLong(ID))
        .type(LogType.valueOf(resultSet.getString(TYPE)))
        .message(resultSet.getString(VALUE))
        .createdAt(resultSet.getObject(CREATED_AT, LocalDateTime.class))
        .build();
  }
}
