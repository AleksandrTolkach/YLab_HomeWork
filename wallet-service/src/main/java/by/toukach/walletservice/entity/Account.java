package by.toukach.walletservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DAO для счета в банке.
 * */
@Data
@Builder
public class Account {

  private Long id;
  private LocalDateTime createdAt;
  private String title;
  private BigDecimal sum;
  private Long userId;
}
