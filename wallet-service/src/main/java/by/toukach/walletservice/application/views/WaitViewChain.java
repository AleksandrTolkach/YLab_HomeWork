package by.toukach.walletservice.application.views;

import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.domain.services.LoggerService;
import by.toukach.walletservice.enumiration.LogType;
import by.toukach.walletservice.infrastructure.entity.Log;
import by.toukach.walletservice.utils.LogUtil;
import java.util.Scanner;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода формы ожидания в консоль.
 * */
@NoArgsConstructor
public class WaitViewChain extends ViewChain {

  public WaitViewChain(UserDto userDto) {
    setUserDto(userDto);
  }

  @Override
  public void handle() {
    LoggerService loggerService = getLoggerService();

    if (getUserDto() != null) {
      Log log = LogUtil.prepareLog(LogType.AUTH,
          String.format(LogUtil.LOGOUT, getUserDto().getId()));
      loggerService.createLog(log);
    }

    System.out.println(ViewMessage.WAITER);

    Scanner scanner = getScanner();
    scanner.nextLine();
    setNextViewChain(new EntryViewChain());
  }
}