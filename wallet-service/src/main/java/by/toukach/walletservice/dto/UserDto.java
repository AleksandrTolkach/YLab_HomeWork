package by.toukach.walletservice.dto;

import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomDeserializer;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomSerializer;
import by.toukach.walletservice.enumiration.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляющий DTO для пользователя.
 * */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"password", "accountList"}, ignoreUnknown = true)
public class UserDto {

  private Long id;
  @JsonSerialize(using = LocalDateTimeCustomSerializer.class)
  @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
  private LocalDateTime createdAt;
  private String login;
  private String password;
  private UserRole role;
  private List<AccountDto> accountList;
}
