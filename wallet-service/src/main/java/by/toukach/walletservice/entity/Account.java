package by.toukach.walletservice.entity;

import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DAO для счета в банке.
 * */
@Data
@Builder
public class Account {

  private Long id;
  private String title;
  private Double sum;
}
