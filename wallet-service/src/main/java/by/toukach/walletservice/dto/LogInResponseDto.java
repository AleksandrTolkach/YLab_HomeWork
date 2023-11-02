package by.toukach.walletservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляющий DTO для ответа на LogIn запрос.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInResponseDto {

  private String accessTokenCookieString;
  private UserDto userDto;
}
