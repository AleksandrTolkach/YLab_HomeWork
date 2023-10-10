package by.toukach.walletservice.infrastructure.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DAO для счета в банке.
 * */
@Data
@Builder
public class AccountEntity {

  private Long id;
  private String title;
  private Double sum;
}
