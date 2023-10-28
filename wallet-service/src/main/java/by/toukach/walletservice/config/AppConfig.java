package by.toukach.walletservice.config;

import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomDeserializer;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class AppConfig {

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
