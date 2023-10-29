package by.toukach.walletservice.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Конфигурационный класс для настройки представления.
 */
@Configuration
public class ViewConfig implements ApplicationContextAware {

  private static final String MESSAGE_BASE_NAME = "Messages";
  private static final String TEMPLATE_PREFIX = "/";
  private static final String TEMPLATE_SUFFIX = ".html";

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * Метод для создания бина ResourceBundleMessageSource.
   *
   * @return запрашиваемый ResourceBundleMessageSource.
   */
  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename(MESSAGE_BASE_NAME);
    return messageSource;
  }

  /**
   * Метод для создания бина SpringResourceTemplateResolver,
   * в котором указаны префикс и суффикс представления.
   *
   * @return запрашиваемый SpringResourceTemplateResolver.
   */
  @Bean
  public SpringResourceTemplateResolver templateResolver() {
    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(applicationContext);
    templateResolver.setPrefix(TEMPLATE_PREFIX);
    templateResolver.setSuffix(TEMPLATE_SUFFIX);
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCacheable(true);
    return templateResolver;
  }

  /**
   * Метод для создания бина SpringTemplateEngine.
   *
   * @return запрашиваемый SpringTemplateEngine.
   */
  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    templateEngine.setEnableSpringELCompiler(true);
    return templateEngine;
  }

  /**
   * Метод для создания бина ThymeleafViewResolver, который работает с представлениями.
   *
   * @return запрашиваемый ThymeleafViewResolver.
   */
  @Bean
  public ThymeleafViewResolver viewResolver() {
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine());
    return viewResolver;
  }
}
