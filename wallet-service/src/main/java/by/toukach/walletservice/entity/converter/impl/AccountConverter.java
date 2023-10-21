package by.toukach.walletservice.entity.converter.impl;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.converter.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для конвертации Account из DTO в DAO и обратно.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountConverter implements Converter<Account, AccountDto> {

  private static final Converter<Account, AccountDto> instance = new AccountConverter();

  @Override
  public Account toEntity(AccountDto dto) {
    return Account.builder()
        .id(dto.getId())
        .createdAt(dto.getCreatedAt())
        .title(dto.getTitle())
        .sum(dto.getSum())
        .userId(dto.getUserId())
        .build();
  }

  @Override
  public AccountDto toDto(Account entity) {
    return AccountDto.builder()
        .id(entity.getId())
        .createdAt(entity.getCreatedAt())
        .title(entity.getTitle())
        .sum(entity.getSum())
        .userId(entity.getUserId())
        .build();
  }

  public static Converter<Account, AccountDto> getInstance() {
    return instance;
  }
}
