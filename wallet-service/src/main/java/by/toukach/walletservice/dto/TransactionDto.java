package by.toukach.walletservice.dto;

import by.toukach.walletservice.enumiration.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DTO для операции со счетом.
 * */
@Data
@Builder
public class TransactionDto {

  private Long id;
  private LocalDateTime createdAt;
  private TransactionType type;
  private Long userId;
  private Long accountId;
  private BigDecimal value;
}
