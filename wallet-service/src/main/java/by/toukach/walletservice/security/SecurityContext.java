package by.toukach.walletservice.security;

import by.toukach.walletservice.dto.UserDto;

/**
 * Класс представляющий SecurityContext, в котором хранится информация о текущем пользователе.
 */
public class SecurityContext {

  private static ThreadLocal<UserDto> currentUser = new ThreadLocal<>();

  public static void setCurrentUser(UserDto userDto) {
    currentUser.set(userDto);
  }

  public static UserDto getCurrentUser() {
    return currentUser.get();
  }

  public static void clearCurrentUser() {
    currentUser.remove();
  }
}
