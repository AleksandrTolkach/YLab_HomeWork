package by.toukach.walletservice.aspect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Аспект для выполнения замера скорости выполнения публичных методов приложения wallet-service.
 */
@Aspect
public class MethodSpeedAspect {

  private static final String DEFINED_TIME = "Для выполнения метода %s.%s затраченное время %s %n";
  private static final String FILE_PATH = "ExecutedMethods.txt";

  @Pointcut("execution(public * *(..))")
  public void calculatedMethod() {
  }

  /**
   * Совет по выполнению замера скорости работы метода.
   *
   * @param proceedingJoinPoint точка присоединения.
   * @return стандартный для выполненного метода возвращаемый объект.
   * @throws Throwable стандартное исключение.
   */
  @Around("calculatedMethod()")
  public Object loggingMethodsTimeExecution(ProceedingJoinPoint proceedingJoinPoint)
      throws Throwable {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    String className = signature.getDeclaringTypeName();
    String methodName = signature.getMethod().getName();

    long startTime = System.currentTimeMillis();
    Object result = proceedingJoinPoint.proceed();
    long endTime = System.currentTimeMillis();

    String filePath = MethodSpeedAspect.class
        .getClassLoader().getResource(FILE_PATH).getPath();

    try (FileWriter writer = new FileWriter(new File(filePath), true)) {
      writer.write(String.format(DEFINED_TIME, className, methodName, endTime - startTime));
    } catch (IOException e) {
      return result;
    }

    return result;
  }
}
