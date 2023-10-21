package by.toukach.walletservice.entity.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Интерфейс для создания Entity из ResultSet.
 *
 * @param <T> объект создания.
 */
public interface RowMapper<T> {

  /**
   * Метод для создания Entity из ResultSet.
   *
   * @param resultSet полученный ResultSet из запроса.
   * @return созданный Entity.
   * @throws SQLException может быть выброшена при получении поля из ResultSet.
   */
  T mapRow(ResultSet resultSet) throws SQLException;
}
