package by.toukach.logger.repository.impl;

import by.toukach.logger.entity.Log;
import by.toukach.logger.repository.LoggerRepository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * Класс для выполнения запросов, связанных с логами, в память.
 */
@Repository
@RequiredArgsConstructor
public class LoggerRepositoryImpl implements LoggerRepository {

  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<Log> logRowMapper;

  @Override
  public Log createLog(Log log) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO application.logs (type, value , created_at) "
              + "VALUES (?, ?, ?) RETURNING ID",
          Statement.RETURN_GENERATED_KEYS);

      statement.setObject(1, log.getType().name());
      statement.setObject(2, log.getMessage());
      statement.setObject(3, log.getCreatedAt());
      return statement;
    }, keyHolder);

    Long id = keyHolder.getKey() != null ? keyHolder.getKeyAs(Long.class) : null;
    log.setId(id);

    return log;
  }

  @Override
  public List<Log> findLogs() {
    return jdbcTemplate
        .query("SELECT id, type, value , created_at FROM application.logs",
            logRowMapper::mapRow);
  }
}
