package by.toukach.walletservice.mapper;

import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс для преобразования User в UserDto и обратно.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper instance = Mappers.getMapper(UserMapper.class);

  /**
   * Метод для преобразования User в UserDto.
   *
   * @param user User для преобразования.
   * @return преобразованный UserDto.
   */
  UserDto userToUserDto(User user);

  /**
   * Метож для преобразования UserDto в User.
   *
   * @param userDto UserDto для преобразования.
   * @return преобразованный User.
   */
  User userDtoToUser(UserDto userDto);
}
