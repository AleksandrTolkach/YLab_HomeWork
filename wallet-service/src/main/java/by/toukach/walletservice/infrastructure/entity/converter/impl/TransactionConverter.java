package by.toukach.walletservice.infrastructure.entity.converter.impl;

import by.toukach.walletservice.domain.models.TransactionDto;
import by.toukach.walletservice.infrastructure.entity.TransactionEntity;
import by.toukach.walletservice.infrastructure.entity.converter.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для конвертации Transaction из DTO в DAO и обратно.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionConverter implements Converter<TransactionEntity, TransactionDto> {

  private static final Converter<TransactionEntity, TransactionDto> instance =
      new TransactionConverter();

  @Override
  public TransactionEntity toEntity(TransactionDto dto) {
    return TransactionEntity.builder()
        .id(dto.getId())
        .type(dto.getType())
        .userId(dto.getUserId())
        .accountId(dto.getAccountId())
        .value(dto.getValue())
        .build();
  }

  @Override
  public TransactionDto toDto(TransactionEntity entity) {
    return TransactionDto.builder()
        .id(entity.getId())
        .type(entity.getType())
        .userId(entity.getUserId())
        .accountId(entity.getAccountId())
        .value(entity.getValue())
        .build();
  }

  public static Converter<TransactionEntity, TransactionDto> getInstance() {
    return instance;
  }
}
