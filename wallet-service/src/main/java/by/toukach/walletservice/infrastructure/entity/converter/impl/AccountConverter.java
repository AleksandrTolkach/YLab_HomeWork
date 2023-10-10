package by.toukach.walletservice.infrastructure.entity.converter.impl;

import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.entity.converter.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для конвертации Account из DTO в DAO и обратно.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountConverter implements Converter<AccountEntity, AccountDto> {

  private static final Converter<AccountEntity, AccountDto> instance = new AccountConverter();

  @Override
  public AccountEntity toEntity(AccountDto dto) {
    return AccountEntity.builder()
        .id(dto.getId())
        .title(dto.getTitle())
        .sum(dto.getSum())
        .build();
  }

  @Override
  public AccountDto toDto(AccountEntity entity) {
    return AccountDto.builder()
        .id(entity.getId())
        .title(entity.getTitle())
        .sum(entity.getSum())
        .build();
  }

  public static Converter<AccountEntity, AccountDto> getInstance() {
    return instance;
  }
}
