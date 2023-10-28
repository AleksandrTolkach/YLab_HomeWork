package by.toukach.walletservice.security.impl;

import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.security.Authentication;
import by.toukach.walletservice.security.AuthenticationManager;
import by.toukach.walletservice.security.SecurityContext;
import by.toukach.walletservice.service.UserService;
import by.toukach.walletservice.service.impl.UserServiceImpl;
import by.toukach.walletservice.utils.JwtUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Класс для выполнения аутентификации пользователей.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements AuthenticationManager {

  private static final String GUEST = "guest";

  private final UserService useService;
  private Map<String, Authentication> authenticationMap = new HashMap<>();

  @Override
  public Authentication getAuthentication(String login) {
    Authentication authentication = authenticationMap.get(login);
    if (authentication != null) {
      boolean isAuthenticated = JwtUtil.validateJwtToken(authentication.getToken());
      authentication.setAuthenticated(isAuthenticated);
    } else {
      authentication = Authentication.builder()
          .login(login)
          .authenticated(false).build();
    }

    UserDto userDto = useService.findUserByLogin(login);
    SecurityContext.setCurrentUser(userDto);
    return authentication;
  }

  @Override
  public Authentication authenticate(String login, String password) {

    Authentication authentication = Authentication.builder()
        .login(GUEST)
        .authenticated(false)
        .build();

    if (login == null || password == null) {
      return authentication;
    }

    UserDto userDto;

    try {
      userDto = useService.findUserByLogin(login);
    } catch (EntityNotFoundException e) {
      userDto = null;
    }

    if (userDto == null || !userDto.getPassword().equals(password)) {
      return authentication;
    } else {
      String jwtToken = JwtUtil.generateTokenFromUsername(login);

      authentication.setLogin(login);
      authentication.setToken(jwtToken);
      authentication.setAuthority(userDto.getRole());
      authentication.setAuthenticated(true);

      authenticationMap.put(login, authentication);

      SecurityContext.setCurrentUser(userDto);

      return authentication;
    }
  }

  @Override
  public void clearAuthentication(String login) {
    authenticationMap.remove(login);
  }
}
