package by.toukach.logger.aspect;

import by.toukach.logger.aspect.annotation.Loggable;
import by.toukach.logger.dto.LogDto;
import by.toukach.logger.enumiration.LogType;
import by.toukach.logger.service.LogProvider;
import by.toukach.logger.service.LogProviderFactory;
import by.toukach.logger.service.LoggerService;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Аспект для выполнения логирования методов, которые помечены аннотацией @Loggable.
 */
@Aspect
public class LoggableAspect {

  @Autowired
  private LoggerService loggerService;
  @Autowired
  private LogProviderFactory logProviderFactory;

  @Pointcut("@annotation(by.toukach.logger.aspect.annotation.Loggable)")
  public void annotatedByLoggable() {
  }

  /**
   * Совет по выполнению логирования.
   *
   * @param proceedingJoinPoint точка привязки.
   * @return стандартный для выполненного метода возвращаемый объект.
   * @throws Throwable стандартное исключение.
   */
  @Around("annotatedByLoggable()")
  public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Method method = signature.getMethod();
    Loggable annotation = method.getAnnotation(Loggable.class);

    LogType logType = annotation.type();

    Object object = proceedingJoinPoint.proceed();

    LogProvider logProvider = logProviderFactory.getProvider(logType);
    LogDto logDto = logProvider.provideLog(object);
    loggerService.createLog(logDto);

    return object;
  }
}
