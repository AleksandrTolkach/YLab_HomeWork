package by.toukach.walletservice.domain.services;

import by.toukach.walletservice.domain.models.LogInDto;
import by.toukach.walletservice.domain.models.SignUpDto;
import by.toukach.walletservice.domain.models.UserDto;

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
