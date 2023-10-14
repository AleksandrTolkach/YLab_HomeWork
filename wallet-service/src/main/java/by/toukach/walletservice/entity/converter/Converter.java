package by.toukach.walletservice.entity.converter;

/**
 * Интерфейс для конвертации DTO в DAO и обратно.
 *
 * @param <E> DAO
 * @param <D> DTO
 */
public interface Converter<E, D> {

  /**
   * Метод, позволяющий конвертировать DTO в DAO.
   *
   * @param dto DTO для конвертации.
   * @return конвертированный в DAO объект.
   */
  E toEntity(D dto);

  /**
   * Метод, позволяющий конвертировать DAO в DTO.
   *
   * @param entity DAO для конвертации.
   * @return конвертированный в DTO объект.
   */
  D toDto(E entity);
}
