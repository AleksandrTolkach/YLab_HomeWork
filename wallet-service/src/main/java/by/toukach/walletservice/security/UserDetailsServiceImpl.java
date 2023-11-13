package by.toukach.walletservice.security;

import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.exception.EntityNotFoundException;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Класс, реализующий UserDetailsService.
 * Загружает пользователя из БД для последующего использования в SecurityContext.
 */
@Service
@Primary
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByLogin(username).orElseThrow(
        () -> new EntityNotFoundException(ExceptionMessage.WRONG_LOGIN_OR_PASSWORD));
    return new UserDetailsImpl(user);
  }
}
