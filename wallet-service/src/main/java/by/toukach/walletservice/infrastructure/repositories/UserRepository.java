package by.toukach.walletservice.infrastructure.repositories;

import by.toukach.walletservice.infrastructure.entity.UserEntity;

/**
 * Интерфейс для выполнения запросов, связанных с пользователями, в память.
 * */
public interface UserRepository {

  /**
   * Метод для создания пользователя в памяти.
   *
   * @param user создаваемый пользователь.
   * @return созданный пользователь.
   */
  UserEntity createUser(UserEntity user);

  /**
   * Метод для чтения пользователя из памяти.
   *
   * @param id id запрашиваемого пользователя.
   * @return запрашиваемый пользователь.
   */
  UserEntity findUserById(Long id);

  /**
   * Метод для чтения пользователя из памяти по логину.
   *
   * @param login логин запрашиваемого пользователя.
   * @return запрашиваемый пользователь.
   */
  UserEntity findUserByLogin(String login);

  /**
   * Метод для проверки существования пользователя в памяти по id.
   *
   * @param id логин для проверки.
   * @return возвращает true, если пользователь существует, и false, если отсутствует.
   */
  boolean isExists(Long id);

  /**
   * Метод для проверки существования пользователя в памяти по логину.
   *
   * @param login логин для проверки.
   * @return возвращает true, если пользователь существует, и false, если отсутствует.
   */
  boolean isExists(String login);
}
