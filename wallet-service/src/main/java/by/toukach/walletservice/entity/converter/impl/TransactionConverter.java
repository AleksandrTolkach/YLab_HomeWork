package by.toukach.walletservice.entity.converter.impl;

import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.entity.Transaction;
import by.toukach.walletservice.entity.converter.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для конвертации Transaction из DTO в DAO и обратно.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionConverter implements Converter<Transaction, TransactionDto> {

  private static final Converter<Transaction, TransactionDto> instance =
      new TransactionConverter();

  @Override
  public Transaction toEntity(TransactionDto dto) {
    return Transaction.builder()
        .id(dto.getId())
        .createdAt(dto.getCreatedAt())
        .type(dto.getType())
        .userId(dto.getUserId())
        .accountId(dto.getAccountId())
        .value(dto.getValue())
        .build();
  }

  @Override
  public TransactionDto toDto(Transaction entity) {
    return TransactionDto.builder()
        .id(entity.getId())
        .createdAt(entity.getCreatedAt())
        .type(entity.getType())
        .userId(entity.getUserId())
        .accountId(entity.getAccountId())
        .value(entity.getValue())
        .build();
  }

  public static Converter<Transaction, TransactionDto> getInstance() {
    return instance;
  }
}
