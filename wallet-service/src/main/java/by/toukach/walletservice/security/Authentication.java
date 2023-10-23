package by.toukach.walletservice.security;

import by.toukach.walletservice.enumiration.UserRole;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий объект аутентификации пользователя.
 */
@Data
@Builder
public class Authentication {

  private String login;
  private String token;
  private UserRole authority;
  private boolean authenticated;
}
