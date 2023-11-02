package by.toukach.walletservice.utils;

import by.toukach.walletservice.security.UserDetailsImpl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Утилитарный класс для работы с SecurityContext.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

  /**
   * Метод для получения данных о пользователе из SecurityContext.
   *
   * @return запрашиваемые данные.
   */
  public static UserDetailsImpl getUserDetails() {
    return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
