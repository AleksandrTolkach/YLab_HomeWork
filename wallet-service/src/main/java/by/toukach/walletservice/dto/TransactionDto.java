package by.toukach.walletservice.dto;

import by.toukach.walletservice.enumiration.TransactionType;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DTO для операции со счетом.
 * */
@Data
@Builder
public class TransactionDto {

  private Long id;
  private TransactionType type;
  private Long userId;
  private Long accountId;
  private Double value;
}
