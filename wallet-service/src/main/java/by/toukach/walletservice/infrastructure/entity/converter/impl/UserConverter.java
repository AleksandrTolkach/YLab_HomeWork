package by.toukach.walletservice.infrastructure.entity.converter.impl;

import by.toukach.walletservice.domain.models.AccountDto;
import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.infrastructure.entity.AccountEntity;
import by.toukach.walletservice.infrastructure.entity.UserEntity;
import by.toukach.walletservice.infrastructure.entity.converter.Converter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для конвертации User из DTO в DAO и обратно.
 * */
public class UserConverter implements Converter<UserEntity, UserDto> {

  private static final Converter<UserEntity, UserDto> instance = new UserConverter();

  private final Converter<AccountEntity, AccountDto> accountConverter;

  private UserConverter() {
    accountConverter = AccountConverter.getInstance();
  }

  @Override
  public UserEntity toEntity(UserDto dto) {
    List<AccountEntity> accountEntityList = dto.getAccountList().stream()
        .map(accountConverter::toEntity)
        .collect(Collectors.toList());

    return UserEntity.builder()
        .id(dto.getId())
        .login(dto.getLogin())
        .password(dto.getPassword())
        .accountList(accountEntityList)
        .build();
  }

  @Override
  public UserDto toDto(UserEntity entity) {
    List<AccountDto> accountDtoList = entity.getAccountList().stream()
        .map(accountConverter::toDto)
        .collect(Collectors.toList());

    return UserDto.builder()
        .id(entity.getId())
        .login(entity.getLogin())
        .password(entity.getPassword())
        .accountList(accountDtoList)
        .build();
  }

  public static Converter<UserEntity, UserDto> getInstance() {
    return instance;
  }
}
