package by.toukach.walletservice.controller.in.rest;

import by.toukach.walletservice.config.AppConfig;
import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInDtoResponse;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final AppConfig appConfig;
  @Value("${app.data}")
  private String driver2;

  @PostMapping("/login")
  public ResponseEntity<UserDto> logIn(@RequestBody LogInDto logInDto) {
    String driver2 = this.driver2;
    return createResponse(authService.logIn(logInDto));
  }

  @PostMapping("/sign-up")
  public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto) {
    return createResponse(authService.signUp(signUpDto));
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logOut(String login) {
    authService.logOut(login);
  }

  private ResponseEntity<UserDto> createResponse(LogInDtoResponse logInDtoResponse) {
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, logInDtoResponse.getAuthentication().getToken())
        .body(logInDtoResponse.getUserDto());
  }
}
