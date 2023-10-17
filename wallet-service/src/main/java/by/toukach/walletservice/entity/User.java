package by.toukach.walletservice.entity;

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
  private List<Account> accountList;
}
