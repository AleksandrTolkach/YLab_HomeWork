package by.toukach.walletservice.dto.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для десериализации ObjectMapper типа LocalDateTime.
 */
public class LocalDateTimeCustomDeserializer extends StdDeserializer<LocalDateTime> {

  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public LocalDateTimeCustomDeserializer() {
    super(LocalDateTime.class);
  }

  @Override
  public LocalDateTime deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException, JacksonException {
    return LocalDateTime.parse(jsonParser.getValueAsString(),
        DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
  }
}
