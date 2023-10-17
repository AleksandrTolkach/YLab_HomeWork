package by.toukach.walletservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DTO для пользователя.
 * */
@Data
@Builder
public class UserDto {

  private Long id;
  private LocalDateTime createdAt;
  private String login;
  private String password;
  private List<AccountDto> accountList;
}
