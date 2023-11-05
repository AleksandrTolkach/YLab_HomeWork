package by.toukach.walletservice.dto;

import by.toukach.walletservice.enumiration.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс представляющий DTO для создания операции со счетом.
 * */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTransactionDto {

  private TransactionType type;
  private Long userId;
  private Long accountId;
  private BigDecimal value;
}
