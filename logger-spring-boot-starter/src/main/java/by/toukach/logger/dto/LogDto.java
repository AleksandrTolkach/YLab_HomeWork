package by.toukach.logger.dto;

import by.toukach.logger.enumiration.LogType;
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
  private LocalDateTime createdAt;
}
