package by.toukach.walletservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DTO для входа в приложение.
 * */
@Data
@Builder
@AllArgsConstructor
public class LogInDto {

  private String login;
  private String password;
}
