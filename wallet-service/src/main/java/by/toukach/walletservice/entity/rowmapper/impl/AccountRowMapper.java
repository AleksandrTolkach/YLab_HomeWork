package by.toukach.walletservice.entity.rowmapper.impl;

import by.toukach.walletservice.entity.Account;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Класс для создания Account из ResultSet.
 * */
@Component
public class AccountRowMapper implements RowMapper<Account> {

  private static final String ID = "account_id";
  private static final String CREATED_AT = "account_created_at";
  private static final String TITLE = "account_title";
  private static final String SUM = "account_sum";
  private static final String USER_ID = "user_id";

  @Override
  public Account mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return Account.builder()
        .id(resultSet.getLong(ID))
        .createdAt(resultSet.getObject(CREATED_AT, LocalDateTime.class))
        .title(resultSet.getString(TITLE))
        .sum(resultSet.getBigDecimal(SUM))
        .userId(resultSet.getLong(USER_ID))
        .build();
  }
}
