package by.toukach.walletservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Класс представляющий DTO для счета.
 * */
@Data
@Builder
public class AccountDto {

  private Long id;
  private String title;
  private Double sum;
}