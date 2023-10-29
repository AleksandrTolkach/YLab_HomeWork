package by.toukach.walletservice.aspect;

import by.toukach.walletservice.aspect.annotation.Loggable;
import by.toukach.walletservice.dto.AccountDto;
import by.toukach.walletservice.dto.LogDto;
import by.toukach.walletservice.dto.LogInResponseDto;
import by.toukach.walletservice.dto.TransactionDto;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.service.LoggerService;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
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

  private LoggerService loggerService;

  @Autowired
  public void setLoggerService(LoggerService loggerService) {
    this.loggerService = loggerService;
  }

  @Pointcut("@annotation(by.toukach.walletservice.aspect.annotation.Loggable)")
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
    if (loggerService == null) {
      return proceedingJoinPoint.proceed();
    }
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Method method = signature.getMethod();
    Loggable annotation = method.getAnnotation(Loggable.class);

    LogType type = annotation.type();

    switch (type) {
      case DEBIT -> {
        return createTransactionLog(LogType.DEBIT, proceedingJoinPoint);
      }
      case CREDIT -> {
        return createTransactionLog(LogType.CREDIT, proceedingJoinPoint);
      }
      case LOG_IN -> {
        return createAuthLog(LogType.LOG_IN, proceedingJoinPoint);
      }
      case SIGN_UP -> {
        return createAuthLog(LogType.SIGN_UP, proceedingJoinPoint);
      }
      case LOG_OUT -> {
        return createAuthLog(LogType.LOG_OUT, proceedingJoinPoint);
      }
      case CREATE_ACCOUNT -> {
        return createAccountLog(LogType.CREATE_ACCOUNT, proceedingJoinPoint);
      }
      default -> {
        return null;
      }
    }
  }

  /**
   * Метод для создания лога о транзакции.
   *
   * @param logType тип лога.
   * @param proceedingJoinPoint точка привязки.
   * @return залогированная транзакция.
   * @throws Throwable исключение при работе с аспектом.
   */
  private TransactionDto createTransactionLog(LogType logType,
      ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    TransactionDto transactionDto = (TransactionDto) proceedingJoinPoint.proceed();
    LogDto logDto = createLog(logType, String.valueOf(transactionDto.getUserId()),
        String.valueOf(transactionDto.getAccountId()),
        String.valueOf(transactionDto.getValue()));
    loggerService.createLog(logDto);
    return transactionDto;
  }

  /**
   * Метод для создания лога об авторизации.
   *
   * @param logType тип лога.
   * @param proceedingJoinPoint точка привязки.
   * @return данные об авторизованном пользователе.
   * @throws Throwable исключение при работе с аспектом.
   */
  private LogInResponseDto createAuthLog(LogType logType, ProceedingJoinPoint proceedingJoinPoint)
      throws Throwable {
    LogInResponseDto logInResponseDto = (LogInResponseDto) proceedingJoinPoint.proceed();
    LogDto logDto = createLog(logType, logInResponseDto.getUserDto().getLogin());
    loggerService.createLog(logDto);
    return logInResponseDto;
  }

  /**
   * Метод для создания лога о выполнении действии со счетом.
   *
   * @param logType тип лога.
   * @param proceedingJoinPoint точка привязки.
   * @return данные об счете.
   * @throws Throwable исключение при работе с аспектом.
   */
  private AccountDto createAccountLog(LogType logType, ProceedingJoinPoint proceedingJoinPoint)
      throws Throwable {
    AccountDto accountDto = (AccountDto) proceedingJoinPoint.proceed();
    LogDto logDto = createLog(logType, String.valueOf(accountDto.getUserId()),
        String.valueOf(accountDto.getId()));
    loggerService.createLog(logDto);
    return accountDto;
  }

  /**
   * Метод для создания объекта лога.
   *
   * @param type тип лога.
   * @param arg информация о логе.
   * @return подготовленный объект лога.
   */
  private LogDto createLog(LogType type, String... arg) {
    String logMessage = String.format(type.getMessage(), arg);

    return LogDto.builder()
        .type(type)
        .message(logMessage)
        .createdAt(LocalDateTime.now())
        .build();
  }
}
