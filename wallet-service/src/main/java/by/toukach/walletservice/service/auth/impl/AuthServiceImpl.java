package by.toukach.walletservice.service.auth.impl;

import by.toukach.logger.aspect.annotation.Loggable;
import by.toukach.logger.enumiration.LogType;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInResponseDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.enumiration.UserRole;
import by.toukach.walletservice.service.auth.AuthService;
import by.toukach.walletservice.service.user.UserService;
import by.toukach.walletservice.utils.CookieUtil;
import by.toukach.walletservice.utils.JwtUtil;
import by.toukach.walletservice.utils.SecurityUtil;
import by.toukach.walletservice.validator.Validator;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

/**
 * Класс для аутентификации пользователей.
 */
@Repository
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final Validator<LogInDto> logInDtoValidator;
  private final Validator<SignUpDto> signUpDtoValidator;
  private final AuthenticationManager authenticationManager;

  @Override
  @Loggable(type = LogType.LOG_IN)
  public LogInResponseDto logIn(LogInDto logInDto) {

    logInDtoValidator.validate(logInDto);

    String login = logInDto.getLogin();
    UserDto userDto = userService.findUserByLogin(login);
    setAuthentication(login, logInDto.getPassword());

    return authenticateUser(userDto);
  }

  @Override
  @Loggable(type = LogType.SIGN_UP)
  public LogInResponseDto signUp(SignUpDto signUpDto) {
    signUpDtoValidator.validate(signUpDto);

    String login = signUpDto.getLogin();
    String password = signUpDto.getPassword();

    UserDto userDto = UserDto.builder()
        .login(login)
        .password(password)
        .role(UserRole.USER)
        .accountList(new ArrayList<>())
        .build();

    userDto = userService.createUser(userDto);

    setAuthentication(login, password);

    return authenticateUser(userDto);
  }

  @Override
  @Loggable(type = LogType.LOG_OUT)
  public LogInResponseDto logOut() {
    Long id = SecurityUtil.getUserDetails().getId();
    UserDto user = userService.findUserById(id);
    return LogInResponseDto.builder()
        .accessTokenCookieString(CookieUtil.getCleanAccessCookie().toString())
        .userDto(user)
        .build();
  }

  private void setAuthentication(String login, String password) {
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(login, password));

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authenticate);
  }

  private LogInResponseDto authenticateUser(UserDto userDto) {
    String accessToken = JwtUtil.generateTokenFromUsername(userDto.getLogin());
    return LogInResponseDto.builder()
        .accessTokenCookieString(CookieUtil.generateAccessCookie(accessToken).toString())
        .userDto(userDto)
        .build();
  }
}
