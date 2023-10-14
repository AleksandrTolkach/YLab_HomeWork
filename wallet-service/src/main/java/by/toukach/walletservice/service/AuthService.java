package by.toukach.walletservice.service;

import by.toukach.walletservice.dto.LogInDto;
import by.toukach.walletservice.dto.SignUpDto;
import by.toukach.walletservice.dto.UserDto;

/**
 * Интерфейс для аутентификации пользователей.
 * */
public interface AuthService {

  /**
   * Метод для входа в приложение.
   *
   * @param logInDto данные пользователя.
   * @return вошедший пользователь.
   */
  UserDto logIn(LogInDto logInDto);

  /**
   * Метод для регистрации пользователя в приложении.
   *
   * @param signUpDto данные пользователя.
   * @return зарегистрированный пользователь.
   */
  UserDto signUp(SignUpDto signUpDto);
}
