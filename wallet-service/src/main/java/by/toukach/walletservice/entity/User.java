package by.toukach.walletservice.entity;

import by.toukach.walletservice.enumiration.UserRole;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DAO для пользователя.
 * */
@Data
@Builder
public class User {

  private Long id;
  private String login;
  private LocalDateTime createdAt;
  private String password;
  private UserRole role;
  private List<Account> accountList;
}
