package by.toukach.walletservice.config;

import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomDeserializer;
import by.toukach.walletservice.dto.serializer.LocalDateTimeCustomSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурационный класс для настройки WebApplication контекста.
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private static final String RESOURCE_PATH = "/*";
  private static final String RESOURCE_LOCATION = "/";
  private static final String SWAGGER_URL = "/swagger";
  private static final String SWAGGER_VIEW = "index";

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

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
        .indentOutput(true);
    converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
  }

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler(RESOURCE_PATH).addResourceLocations(RESOURCE_LOCATION);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(SWAGGER_URL).setViewName(SWAGGER_VIEW);
  }
}
