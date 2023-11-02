package by.toukach.walletservice.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Конфигурационный класс для настройки DispatcherServlet.
 */
public class MainWebAppInitializer implements WebApplicationInitializer {

  public static final String BASE_PACKAGE = "by.toukach.walletservice";
  private static final String SERVLET_NAME = "mvc";
  private static final int LOAD_ON_START_UP = 1;
  private static final String SERVLET_MAPPING = "/";

  @Override
  public void onStartup(ServletContext container) throws ServletException {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.scan(BASE_PACKAGE);
    container.addListener(new ContextLoaderListener(context));
    ServletRegistration.Dynamic dispatcher =
        container.addServlet(SERVLET_NAME, new DispatcherServlet(context));
    dispatcher.setLoadOnStartup(LOAD_ON_START_UP);
    dispatcher.addMapping(SERVLET_MAPPING);
  }
}
