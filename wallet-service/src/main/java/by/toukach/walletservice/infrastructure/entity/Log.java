package by.toukach.walletservice.infrastructure.entity;

import by.toukach.walletservice.enumiration.LogType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DAO для лога.
 */
@Data
@Builder
public class Log {

  private Long id;
  private LogType type;
  private String value;
  private LocalDateTime createdAt;
}
