package by.toukach.walletservice.controller.in.rest;

import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInResponseDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с запросами. связанными с аутентификацией пользователя.
 */
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * Метод для обработки запроса на вход в приложение.
   *
   * @param logInDto объект с данными для входа.
   * @return аутентифицированный пользователь.
   */
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> logIn(@RequestBody(required = false) LogInDto logInDto) {
    LogInResponseDto logInResponseDto = authService.logIn(logInDto);
    return createResponse(logInResponseDto);
  }

  /**
   * Метод для обработки запроса на регистрацию в приложении.
   *
   * @param signUpDto объект с данными для регистрации.
   * @return зарегестрированный пользователь.
   */
  @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDto> signUp(@RequestBody(required = false) SignUpDto signUpDto) {
    return createResponse(authService.signUp(signUpDto));
  }

  /**
   * Метод для обработки запроса на выход из приложения.
   *
   * @return статус 204.
   */
  @PostMapping("/logout")
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public ResponseEntity<UserDto> logOut() {
    return ResponseEntity.noContent()
        .header(HttpHeaders.SET_COOKIE, authService.logOut().getAccessTokenCookieString())
        .build();
  }

  /**
   * Метод для подготовки ответа на запрос.
   *
   * @param logInResponseDto данный для тела ответа.
   * @return подготовленный ответ.
   */
  private ResponseEntity<UserDto> createResponse(LogInResponseDto logInResponseDto) {
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, logInResponseDto.getAccessTokenCookieString())
        .body(logInResponseDto.getUserDto());
  }
}
