package by.toukach.walletservice.security;

/**
 * Интерфейс для выполнения аутентификации пользователя.
 */
public interface AuthenticationManager {

  /**
   * Метод для возврата объекта аутентификации пользователя.
   *
   * @param login login аутентифицированного пользователя.
   * @return запрашиваемая аутентификация.
   */
  Authentication getAuthentication(String login);

  /**
   * Метод для аутентификации пользователя в приложении.
   *
   * @param login login пользователя.
   * @param password пароль пользователя.
   * @return объект аутентификации.
   */
  Authentication authenticate(String login, String password);

  /**
   * Удалить записи об аутентификации пользователя.
   *
   * @param login логин пользователя.
   */
  void clearAuthentication(String login);
}
