package by.toukach.walletservice.entity.mapper.impl;

import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.entity.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания User из ResultSet.
 * */
public class UserMapper implements RowMapper<User> {

  private static final RowMapper<User> instance = new UserMapper();
  private static final String ID = "id";
  private static final String CREATED_AT = "created_at";
  private static final String LOGIN = "login";
  private static final String PASSWORD = "password";

  private final RowMapper<Account> accountRowMapper;

  private UserMapper() {
    accountRowMapper = AccountMapper.getInstance();
  }

  @Override
  public User mapRow(ResultSet resultSet) throws SQLException {
    User user = User.builder()
        .id(resultSet.getLong(ID))
        .createdAt(resultSet.getObject(CREATED_AT, LocalDateTime.class))
        .login(resultSet.getString(LOGIN))
        .password(resultSet.getString(PASSWORD))
        .build();

    List<Account> accountList = new ArrayList<>();
    user.setAccountList(accountList);

    Account account = accountRowMapper.mapRow(resultSet);
    if (account.getId() == 0) {
      return user;
    } else {
      accountList.add(account);
      while (resultSet.next()) {
        accountList.add(accountRowMapper.mapRow(resultSet));
      }
    }

    return user;
  }

  public static RowMapper<User> getInstance() {
    return instance;
  }
}
