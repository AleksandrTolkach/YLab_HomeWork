package by.toukach.logger.entity.mapper;

import by.toukach.logger.dto.LogDto;
import by.toukach.logger.entity.Log;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Интерфейс для преобразования Log в LogDto и обратно.
 */
@Mapper(componentModel = "spring")
public interface LogMapper {

  LogMapper instance = Mappers.getMapper(LogMapper.class);

  /**
   * Метод для преобразования Log в LogDto.
   *
   * @param log Log для преобразования.
   * @return преобразованный LogDto.
   */
  LogDto logToLogDto(Log log);

  /**
   * Метод для преобразования LogDto в Log.
   *
   * @param logDto LogDto для преобразования.
   * @return преобразованный Log.
   */
  Log logDtoToLog(LogDto logDto);
}
