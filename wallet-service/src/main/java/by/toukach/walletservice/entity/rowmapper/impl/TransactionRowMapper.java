package by.toukach.walletservice.entity.rowmapper.impl;

import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.enumiration.TransactionType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

/**
 * Класс для создания Transaction из ResultSet.
 * */
@Component
public class TransactionRowMapper implements RowMapper<Transaction> {

  private static final String ID = "id";
  private static final String CREATED_AT = "created_at";
  private static final String TYPE = "type";
  private static final String USER_ID = "user_id";
  private static final String ACCOUNT_ID = "account_id";
  private static final String VALUE = "value";

  @Override
  public Transaction mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return Transaction.builder()
        .id(resultSet.getLong(ID))
        .createdAt(resultSet.getObject(CREATED_AT, LocalDateTime.class))
        .type(TransactionType.valueOf(resultSet.getString(TYPE)))
        .userId(resultSet.getLong(USER_ID))
        .accountId(resultSet.getLong(ACCOUNT_ID))
        .value(resultSet.getBigDecimal(VALUE))
        .build();
  }
}
