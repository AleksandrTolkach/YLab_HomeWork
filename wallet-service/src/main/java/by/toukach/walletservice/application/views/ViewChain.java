package by.toukach.walletservice.application.views;

import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.LoggerService;
import by.toukach.walletservice.domain.services.impl.LoggerServiceImpl;
import java.util.Scanner;
import lombok.Data;

/**
 * Класс для вывода формы с данными в консоль.
 * */
@Data
public abstract class ViewChain {

  private ViewChain nextViewChain;
  private UserDto userDto;
  private Scanner scanner = new Scanner(System.in);
  private final LoggerService loggerService = LoggerServiceImpl.getInstance();

  /**
   * Метод для обработки формы с данными.
   * */
  public abstract void handle();

  /**
   * Метод для получения LoggerService.
   *
   * @return запрашиваемый LoggerService.
   */
  public LoggerService getLoggerService() {
    return loggerService;
  }
}
