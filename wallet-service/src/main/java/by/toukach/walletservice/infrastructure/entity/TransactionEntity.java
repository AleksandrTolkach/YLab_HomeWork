package by.toukach.walletservice.infrastructure.entity;

import by.toukach.walletservice.enumiration.TransactionType;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DAO для операции.
 * */
@Data
@Builder
public class TransactionEntity {

  private Long id;
  private TransactionType type;
  private Long userId;
  private Long accountId;
  private Double value;
}
