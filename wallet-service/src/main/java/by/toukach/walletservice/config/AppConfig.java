package by.toukach.walletservice.config;

import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomDeserializer;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки Приложения.
 */
@Configuration
public class AppConfig {

  /**
   * Метод для создания бина ObjectMapper, который выполняет преобразование JSON в POJO и обратно.
   *
   * @return запрашиваемый ObjectMapper.
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule serializeModule = new SimpleModule();
    serializeModule.addSerializer(LocalDateTime.class, new LocalDateTimeCustomSerializer());
    SimpleModule deSerializeModule = new SimpleModule();
    deSerializeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeCustomDeserializer());

    objectMapper.findAndRegisterModules();
    objectMapper.registerModule(serializeModule);
    objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
        true);
    return objectMapper;
  }
}
