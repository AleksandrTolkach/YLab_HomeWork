package by.toukach.walletservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DTO для регистрации в приложении.
 * */
@Data
@Builder
public class SignUpDto {

  private String login;
  private String password;
}
