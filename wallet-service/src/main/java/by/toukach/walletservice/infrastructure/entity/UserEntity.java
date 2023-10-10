package by.toukach.walletservice.infrastructure.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DAO для пользователя.
 * */
@Data
@Builder
public class UserEntity {

  private Long id;
  private String login;
  private String password;
  private List<AccountEntity> accountList;
}
