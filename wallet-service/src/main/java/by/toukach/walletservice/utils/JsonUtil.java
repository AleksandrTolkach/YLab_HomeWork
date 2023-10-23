package by.toukach.walletservice.utils;

import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomDeserializer;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomSerializer;
import by.toukach.walletservice.exception.ExceptionMessage;
import by.toukach.walletservice.exception.JsonMapperException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Утилитарный класс для работы с Json.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {
  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    SimpleModule serializeModule = new SimpleModule();
    serializeModule.addSerializer(LocalDateTime.class, new LocalDateTimeCustomSerializer());
    SimpleModule deSerializeModule = new SimpleModule();
    deSerializeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeCustomDeserializer());

    objectMapper.findAndRegisterModules();
    objectMapper.registerModule(serializeModule);
    objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
        true);
  }

  /**
   * Метод для преобразования Json в объект.
   *
   * @param json json для преобразования.
   * @param metaData данные о классе.
   * @param <T> тип, в который необходимо преобразовать.
   * @return преобразованный объект.
   */
  public static <T> T mapToPojo(String json, Class<T> metaData) {
    try {
      return objectMapper.readValue(json, metaData);
    } catch (JsonProcessingException e) {
      throw new JsonMapperException(ExceptionMessage.POJO_MAP, e);
    }
  }

  /**
   * Метод для преобразования объекта в Json.
   *
   * @param item объект для преобразования.
   * @param <I> тип объекта.
   * @return преобразованный Json.
   */
  public static <I> String mapToJson(I item) {
    try {
      return objectMapper.writeValueAsString(item);
    } catch (JsonProcessingException e) {
      throw new JsonMapperException(ExceptionMessage.JSON_MAP, e);
    }
  }
}
