package by.toukach.walletservice.config;

import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Конфигурационный класс для определения пакета с ресурсами для Swagger.
 */
@Provider
public class MyResourceConfig extends ResourceConfig {

  public MyResourceConfig() {
    packages(true, MainWebAppInitializer.BASE_PACKAGE);
  }
}
