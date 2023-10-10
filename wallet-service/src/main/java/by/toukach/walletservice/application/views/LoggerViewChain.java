package by.toukach.walletservice.application.views;

import by.toukach.walletservice.domain.models.UserDto;
import by.toukach.walletservice.infrastructure.entity.Log;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода формы со списком логов в консоль.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggerViewChain extends ViewChain {

  public LoggerViewChain(UserDto userDto) {
    setUserDto(userDto);
  }

  @Override
  public void handle() {
    List<Log> logList = getLoggerService().findLogs();

    System.out.printf(ViewMessage.LOG_TABLE_HEADER);
    logList.forEach(l -> System.out.printf(ViewMessage.LOG_TABLE_ENTRY,
        l.getType(), l.getValue(), l.getCreatedAt()));

    setNextViewChain(new AdminActionViewChain(getUserDto()));
  }
}
