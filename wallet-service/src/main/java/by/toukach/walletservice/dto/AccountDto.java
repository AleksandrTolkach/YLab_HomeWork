package by.toukach.walletservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DTO для счета.
 * */
@Data
@Builder
public class AccountDto {

  private Long id;
  private LocalDateTime createdAt;
  private String title;
  private BigDecimal sum;
  private Long userId;
}
