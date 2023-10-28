package by.toukach.walletservice.entity.rowmapper.impl;

import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.entity.rowmapper.RowMapper;
import by.toukach.walletservice.enumiration.UserRole;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Класс для создания User из ResultSet.
 * */
@Component
@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<User> {

  private static final String ID = "id";
  private static final String CREATED_AT = "created_at";
  private static final String LOGIN = "login";
  private static final String PASSWORD = "password";
  private static final String ROLE = "role";

  private final RowMapper<Account> accountRowMapper;

  @Override
  public User mapRow(ResultSet resultSet) throws SQLException {
    User user = User.builder()
        .id(resultSet.getLong(ID))
        .createdAt(resultSet.getObject(CREATED_AT, LocalDateTime.class))
        .login(resultSet.getString(LOGIN))
        .password(resultSet.getString(PASSWORD))
        .role(UserRole.valueOf(resultSet.getString(ROLE)))
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
}
