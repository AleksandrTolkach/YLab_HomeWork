package by.toukach.walletservice.entity;

import by.toukach.walletservice.enumiration.LogType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляющий DAO для лога.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {

  private Long id;
  private LogType type;
  private String message;
  private LocalDateTime createdAt;
}
