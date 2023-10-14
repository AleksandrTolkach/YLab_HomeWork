package by.toukach.walletservice.entity.converter.impl;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.Account;
import by.toukach.walletservice.entity.User;
import by.toukach.walletservice.entity.converter.Converter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для конвертации User из DTO в DAO и обратно.
 * */
public class UserConverter implements Converter<User, UserDto> {

  private static final Converter<User, UserDto> instance = new UserConverter();

  private final Converter<Account, AccountDto> accountConverter;

  private UserConverter() {
    accountConverter = AccountConverter.getInstance();
  }

  @Override
  public User toEntity(UserDto dto) {
    List<Account> accountList = dto.getAccountList().stream()
        .map(accountConverter::toEntity)
        .collect(Collectors.toList());

    return User.builder()
        .id(dto.getId())
        .login(dto.getLogin())
        .password(dto.getPassword())
        .accountList(accountList)
        .build();
  }

  @Override
  public UserDto toDto(User entity) {
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

  public static Converter<User, UserDto> getInstance() {
    return instance;
  }
}
