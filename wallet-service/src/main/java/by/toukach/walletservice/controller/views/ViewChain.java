package by.toukach.walletservice.controller.views;

import by.toukach.walletservice.dto.UserDto;
import by.toukach.walletservice.service.LoggerService;
import by.toukach.walletservice.service.impl.LoggerServiceImpl;
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
