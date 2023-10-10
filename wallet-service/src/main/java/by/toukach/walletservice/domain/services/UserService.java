package by.toukach.walletservice.domain.services;

import by.toukach.walletservice.domain.models.UserDto;

/**
 * Интерфейс для выполнения операции с пользователями.
 * */
public interface UserService {

  /**
   * Метод для создания пользователя.
   *
   * @param user создаваемый пользователь.
   * @return созданный пользователь.
   */
  UserDto createUser(UserDto user);

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
   * Метод для обновления пользовательских данных.
   *
   * @param user обновляемый пользователь.
   * @return обновленный пользователь.
   */
  UserDto updateUser(UserDto user);

  /**
   * Метод для проверки существования пользователя.
   *
   * @param id id для проверки.
   * @return возвращает true, если пользователь существует, и false, если нет.
   */
  boolean isExists(Long id);
}
