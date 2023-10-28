package by.toukach.walletservice.entity.mapper;

import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс для преобразования Account в AccountDto и обратно.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

  AccountMapper instance = Mappers.getMapper(AccountMapper.class);

  /**
   * Метод для преобразования Account в AccountDto.
   *
   * @param account Account для преобразования.
   * @return преобразованный AccountDto.
   */
  AccountDto accountToAccountDto(Account account);

  /**
   * Метод для преобразования AccountDto в Account.
   *
   * @param accountDto AccountDto для преобразования.
   * @return преобразованный Account.
   */
  Account accountDtoToAccount(AccountDto accountDto);
}
