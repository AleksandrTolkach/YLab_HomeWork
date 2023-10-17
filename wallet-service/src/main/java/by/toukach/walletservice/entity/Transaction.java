package by.toukach.walletservice.entity;

import by.toukach.walletservice.enumiration.TransactionType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DAO для операции.
 * */
@Data
@Builder
public class Transaction {

  private Long id;
  private LocalDateTime createdAt;
  private TransactionType type;
  private Long userId;
  private Long accountId;
  private Double value;
}
