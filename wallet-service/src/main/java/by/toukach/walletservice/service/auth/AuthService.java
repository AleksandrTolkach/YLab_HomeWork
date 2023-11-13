package by.toukach.walletservice.service.auth;

import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.LogInResponseDto;
import by.toukach.walletservice.dto.SignUpDto;

/**
 * Интерфейс для аутентификации пользователей.
 * */
public interface AuthService {

  /**
   * Метод для входа в приложение.
   *
   * @param logInDto данные пользователя.
   * @return вошедший пользователь и объект аутентификации.
   */
  LogInResponseDto logIn(LogInDto logInDto);

  /**
   * Метод для регистрации пользователя в приложении.
   *
   * @param signUpDto данные пользователя.
   * @return зарегистрированный пользователь и объект аутентификации.
   */
  LogInResponseDto signUp(SignUpDto signUpDto);


  /**
   * Метор для регистрации выхода польователя из приложения.
   *
   * @return вышедший пользователь и объект аутентификации.
   */
  LogInResponseDto logOut();
}
