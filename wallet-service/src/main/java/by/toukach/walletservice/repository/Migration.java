package by.toukach.walletservice.repository;

/**
 * Интерфейс для работы с инструментами миграции БД.
 */
public interface Migration {

  /**
   * Откат внесенных изменений до определенного тега.
   *
   * @param tag тег, то которого необходимо откатить БД.
   */
  void rollback(String tag);
}
