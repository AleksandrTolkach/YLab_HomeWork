package by.toukach.walletservice.dto;

import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomDeserializer;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomSerializer;
import by.toukach.walletservice.enumiration.LogType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляющий DTO для лога.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDto {

  private Long id;
  private LogType type;
  private String message;
  @JsonSerialize(using = LocalDateTimeCustomSerializer.class)
  @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
  private LocalDateTime createdAt;
}
