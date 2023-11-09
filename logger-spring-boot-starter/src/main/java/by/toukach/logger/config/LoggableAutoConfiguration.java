package by.toukach.logger.config;

import by.toukach.logger.aspect.LoggableAspect;
import by.toukach.logger.entity.mapper.LogMapperImpl;
import by.toukach.logger.entity.rowmapper.LogRowMapper;
import by.toukach.logger.repository.LoggerRepository;
import by.toukach.logger.repository.impl.LoggerRepositoryImpl;
import by.toukach.logger.service.LogProvider;
import by.toukach.logger.service.LogProviderFactory;
import by.toukach.logger.service.LoggerService;
import by.toukach.logger.service.impl.LogProviderFactoryImpl;
import by.toukach.logger.service.impl.LoggerServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Конфигурационный файл для настройки модуля логирования.
 */
@Configuration
@RequiredArgsConstructor
public class LoggableAutoConfiguration {

  private final JdbcTemplate jdbcTemplate;
  private final List<LogProvider> logProviderList;

  /**
   * Метод для создания бина LogRowMapper,
   * который выполняет преобразования полученных записей из БД в Log.
   *
   * @return запрашиваемый LogRowMapper.
   */
  @Bean
  public RowMapper logRowMapper() {
    return new LogRowMapper();
  }

  /**
   * Метод для создания бина LoggableAspect,
   * который выполняет логирования методов с аннотацией @Loggable.
   *
   * @return запрашиваемый LoggableAspect.
   */
  @Bean
  public LoggableAspect loggableAspect() {
    return new LoggableAspect();
  }

  /**
   * Метод для создания бина LoggerRepository, который выполняет запросы в БД, связанные с логами.
   *
   * @return запрашиваемый LoggerRepository.
   */
  @Bean
  public LoggerRepository loggerRepository() {
    return new LoggerRepositoryImpl(jdbcTemplate, logRowMapper());
  }

  /**
   * Метод для создания бина LoggerService, который работает с логами.
   *
   * @return запрашиваемый LoggerService.
   */
  @Bean
  public LoggerService loggerService() {
    return new LoggerServiceImpl(loggerRepository(), new LogMapperImpl());
  }

  /**
   * Метод для создания LogProviderFactory, который предоставляет запрашиваемый LogProvider.
   *
   * @return запрашиваемый LogProviderFactory.
   */
  @Bean
  public LogProviderFactory logProviderFactory() {
    return new LogProviderFactoryImpl(logProviderList);
  }
}
