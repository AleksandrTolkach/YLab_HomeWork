package by.toukach.walletservice.dto;

import by.toukach.walletservice.security.Authentication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DTO для ответа на LogIn запрос.
 */
@Data
@Builder
@AllArgsConstructor
public class LogInDtoResponse {

  private Authentication authentication;
  private UserDto userDto;
}
