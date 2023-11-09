package by.toukach.walletservice.service.user;

import by.toukach.walletservice.dto.UserDto;

/**
 * Интерфейс для выполнения операции с пользователями.
 * */
public interface UserService {

  /**
   * Метод для создания пользователя.
   *
   * @param userDto создаваемый пользователь.
   * @return созданный пользователь.
   */
  UserDto createUser(UserDto userDto);

  /**
   * Метод для чтения пользователя по id.
   *
   * @param id id запрашиваемого пользователя.
   * @return запрашиваемый пользователь.
   */
  UserDto findUserById(Long id);

  /**
   * Метод для чтения пользователя по логину.
   *
   * @param login логин запрашиваемого пользователя.
   * @return запрашиваемый пользователь.
   */
  UserDto findUserByLogin(String login);

  /**
   * Метод для проверки существования пользователя.
   *
   * @param id id для проверки.
   * @return возвращает true, если пользователь существует, и false, если нет.
   */
  boolean isExists(Long id);
}
